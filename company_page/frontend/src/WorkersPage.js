import React, { useState, useEffect } from "react";
import "./WorkersPage.css";
import WorkerSection from "./components/WorkerSection";
import Footer from "./components/Footer";
import Header from "./components/Header"
import RegistrationForm from "./components/RegistrationForm";
import BackendURL from "./BackendURL";

function WorkersPage() {
    const [workers,setWorkers] = useState([]);
    const [error,setError] = useState(null);
    const [loading,setLoading] = useState(true);
    
    useEffect(()=>{
        const backendurl = BackendURL();
        fetch(`http://${backendurl}/workers`,{
            headers:{
                'Content-Type':'application/json',
                'Instance-Id': localStorage.getItem('instanceId'),
        }})

        .then((response)=>{
            if(!response.ok){
                throw new Error("Network response was not ok");
            }
            return response.json();
        })
        .then((data)=>{
            setWorkers(data);
            setLoading(false);
        })
        .catch((error)=>{
            setError(error.message);
            setLoading(false);
        });
    },[]);
    let x = workers.length;
    return (
        <div className="App">
            <Header />
            <div className="page">
                <div className="content">
                    {workers.slice(0,x).map((worker,index)=>(
                        <WorkerSection key={index} worker={worker}/>
                    ))}
                </div>
                <div className="regform">
                    <RegistrationForm/>
                </div>
            </div>
            <Footer />
        </div>
    );
}

export default WorkersPage;