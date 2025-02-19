import React, { useState } from 'react';
import './RegisterPage.css';
import { Navigate, redirect, useNavigate } from 'react-router-dom';
import Header from './components/BankHeader';
import Footer from './components/BankFooter';
import Person from './components/registerpage_body_components/km.png';
import BackendURL from './BackendURL';
import DollarBackground from './components/DollarBackground';
const RegisterPage = () => {

    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [repeatPassword, setRepeatPassword] = useState('');
    const [error, setError] = useState(false);
    const [match, setMatch] = useState(true);
    const [message, setMessage] = useState('');
    const [success, setSuccess] = useState(false);
    const navigate = useNavigate();

    const handleReturnClick = () => {
        navigate('/');
    };
    const loginChange = (event) => {
        setLogin(event.target.value);
    };
    const passwordChange = (event) => {
        const newPassword = event.target.value;
        setPassword(newPassword);
        setMatch(newPassword === repeatPassword);
    };
    const repeatPasswordChange = (event) => {
        const newRepeatPassword = event.target.value;
        setRepeatPassword(newRepeatPassword);
        setMatch(password === newRepeatPassword);
    };
    const delay = async (ms) => {
        return new Promise((resolve) => 
            setTimeout(resolve, ms));
    };
    const request_auth_signup = async (username, password) => {
        const data = { username, password };
        const backendurl = await BackendURL();
        try {
            const response = await fetch(`http://${backendurl}/auth/signup`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Instance-Id': localStorage.getItem('instanceId'),
                },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                const result = await response.json();
                setSuccess(true);
                setError(false);
                setMatch(true);
                setPassword('');
                setLogin('');
                setRepeatPassword('');
                await delay(2137);
                navigate('/');
            } else {
                const errorData = await response.json();
                setSuccess(false);
            }
        } catch (error) {
            console.error('Error: ', error);
            setSuccess(false);
        }
    };
    const handleRegister = (e) => {
        e.preventDefault();
        if (match === true) {
            request_auth_signup(login, password);
        }
    }
    const gradientAngle = 90;
    const gradientColors = ['rgba(119,41,41,1)', 'rgba(183,187,143,1)', 'rgba(0,112,255,1)'];
    return (
        <div className="page">
            <Header tokenPresent={false} username="" />
            <DollarBackground multiplier={0} gradientColors={gradientColors} gradientAngle={gradientAngle}/>
            <div className="registerContent">
                <p className="wruc clickable-reg" onClick={handleReturnClick}>&lt; return to main page</p>
                <div className="panels">
                    <div className="panel">

                        <form className="reg-form" onSubmit={handleRegister}>
                            <h1 className="register-text">Register now!</h1>

                            <input
                                className={error ? 'errorInput' : ''}
                                type="text"
                                name="login"
                                placeholder="login"
                                value={login}
                                onChange={loginChange}
                                required />

                            <input
                                className={error ? 'errorInput' : ''}
                                type="password"
                                name="password"
                                placeholder="password"
                                value={password}
                                onChange={passwordChange}
                                required />

                            <input
                                className={error ? 'errorInput' : ''}
                                type="password"
                                name="confirm-password"
                                placeholder="confirm-password"
                                value={repeatPassword}
                                onChange={repeatPasswordChange}
                                required />
                                
                            {!match && <p className="errorMessage">Given passwords are different!</p>}
                            {error && <p className="errorMessage">Data is incorrect!</p>}
                            {success && <p className="successReturn" onClick={handleReturnClick}>Registered successfully! Redirecting to the main page...</p>}
                            <button type="submit" className="register-button clickable-reg">Register</button>
                        </form>
                    </div>
                    <div className="other-content">
                        <div className="img-container">
                            <img src={Person} alt="content" />
                            <div className="text-overlay">I created an account at SIGSEGV NB and I'm extremely satisfied!</div>
                        </div>
                    </div>
                </div>
            </div>
            <Footer />
        </div>
    )
}

export default RegisterPage;