import React, { useState, useEffect } from 'react';
import Banner from "./Banner";
import DraggableWindow from "./DraggableWindow";
import LogoComponent from "./LogoComp";
import Clock from "./Clock";
import TodoList from "./ToDoList";
import Logout from "./Logout";
import CustomBrowser from "./CustomBrowser";
import MailList from "./Mail";
import { useNavigate } from 'react-router-dom';
import BannerWait from './BannerWait';
import AnimationButton from './AnimationButton';
import Points from './Points.js'

const MainPage = ({ fetchUserMe , username}) => {
    
    const navigate = useNavigate();
    const Instance = {
        FORUM: 'Forum',
        MESS: 'Messenger',
        BANK: 'Bank',
        COMPANY: 'Company',
        NONE: 'Home',
    };

    const [InstanceRun, setInstanceRun] = useState(Instance.NONE);
    const [InstanceWait, setInstanceWait] = useState(false);
    const [InstanceUrl, setInstanceUrl] = useState(null);
    const [InstanceTimeLeft, setInstanceTimeLeft] = useState(null);
    const mailListRef = React.useRef(null);
    const triggerMailRequest = () => {
        if(mailListRef.current){
        mailListRef.current.request_mails();
        }
    }
    const extendTime = async () => {
        const token = localStorage.getItem('token');
        if (!token) {
            console.error("Nie ma tokena");
            return;
        }
        console.log("Attempting to reset timer to 1 hour... ");
        try {
            const res = await fetch(`http://localhost:8080/instances/extendTimeout`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (res.ok) {
                const data = await res.json();
                setInstanceTimeLeft(data.secondsRemaining);
                // tutaj zrobic by sie wyswietlalo na bannerze
            } else {
                const errorData = await res.json();
                console.error('Error occured (timeout extending):', errorData);
                if (errorData.status == 400){
                    return;
                }
            }
        } catch (error) {
            console.error("jakis inny error!!!");
        }
    };

    const startInstance = async (instance) => {
        const token = localStorage.getItem('token');
        if(InstanceRun !== Instance.NONE){
            return;
        }
        setInstanceWait(true);
        console.log("DEBUG");
        console.log(token);
        console.log(instance);
        if (!token) {
            console.error("Nie ma tokena");
            return;
        }

        try {
            const res = await fetch(`http://api.home.ctfgame.pl/instances/runLevel?levelName=${instance}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (res.ok) {
                const data = await res.json();
                setInstanceWait(false);
                setInstanceUrl(data.URL);
                setInstanceRun(instance);
                setInstanceTimeLeft(3600);
                console.log("wystarowala instancja:", data);
                // tutaj zrobic by sie wyswietlalo na bannerze
            } else {
                const errorData = await res.json();
                console.error('blad startujac instancje:', errorData);
                if (errorData.status == 400){
                    return;
                }
                setInstanceWait(false);
                // tutaj wyswietlic jakis error
            }
        } catch (error) {
            setInstanceWait(false);
            console.error("jakis inny error!!!");
        }
    };

    const stopInstance = async () => {
        setInstanceRun(Instance.NONE);
        const token = localStorage.getItem('token');
        if (!token) {
            console.error('No token found. User is not authenticated.');
            return;
        }

        try {
            const response = await fetch(`http://api.home.ctfgame.pl/instances/stopLevel`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                const data = await response.json();
                console.log('Instance stopped:', data);
                //alert(`Instance stopped: ${data.message}`);
            } else {
                const errorData = await response.json();
                console.error('Error stopping instance:', errorData);
                // alert(`Failed to stop instance: ${errorData.message}`);
            }
        } catch (error) {
            console.error('Network or server error:', error);
        }
    };

    const instanceStatus = async () => {
        const token = localStorage.getItem('token');
        if (!token) {
            console.error('No token found. User is not authenticated.');
        }
        try {
            const response = await fetch(`http://api.home.ctfgame.pl/instances/status`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                const data = await response.json();
                if (data.secondsRemaining > 0 && data.isRunning == true) {
                    console.log(data);
                    setInstanceRun(data.levelName);
                    setInstanceUrl(data.url);
                    setInstanceTimeLeft(data.secondsRemaining);
                }
                else {
                    setInstanceRun(Instance.NONE);
                }
            } else {
                const errorData = await response.json();
                switch (response.status) {
                  case 403:
                    console.error('403 Forbidden: The user is not authenticated.');
                    break;
                  case 401:
                    console.error('401 Unauthorized: JWT token is invalid.');
                    break;
                  case 400:
                    console.error('400 Bad Request: No level is currently running.');
                    break;
                }
                setInstanceRun(Instance.NONE);
            }
        } catch (error) {
            console.error('Network or server error:', error);
            setInstanceRun(Instance.NONE);
        }
    };

    const handleLogout = () => {
        stopInstance();
        localStorage.removeItem("token");
        navigate("/login");
    };

    useEffect(() => {
        const checkAuthentication = async () => {
            const isAuthenticated = await fetchUserMe();
            if (isAuthenticated) {
            } else {
                navigate("/login");
            }
        };
        
        checkAuthentication();
        instanceStatus();
    }, [navigate]);

    useEffect(() => {
        const interval = setInterval(() => {
            instanceStatus();
        }, 60000);

        return () => clearInterval(interval);
    }, []);

    return (
        <>
            {InstanceWait === true && (<BannerWait />)
            }
            {InstanceRun !== Instance.NONE && (
                <Banner link={InstanceUrl} InstanceRun={InstanceRun} Instance={Instance} stopInstance={stopInstance} timeLeftInstance={InstanceTimeLeft} extendTime={extendTime}/>
            )}
            <DraggableWindow defaultPositionPercent={{ x: 10, y: 10 }}>
                <h1>Mailed it! &#x1f485;</h1>
                <MailList ref={mailListRef}/>
            </DraggableWindow>
            <DraggableWindow defaultPositionPercent={{ x: 70, y: 10 }}  style={{ width: '350px' }} >
                <TodoList onFlagCorrect={triggerMailRequest}/>
            </DraggableWindow>
            <LogoComponent defaultPositionPercent={{ x: 2, y: 82 }} text="Aqua Messenger" startInstance={startInstance} />
            <Clock />
            <DraggableWindow defaultPositionPercent={{ x: 40, y: 60 }} style={{ width: '450px' }}>
                <CustomBrowser startInstance={startInstance} />
            </DraggableWindow>
            <Logout handleLogout={handleLogout} />
            <Points username={username}/>
        </>
    );
}

export default MainPage;