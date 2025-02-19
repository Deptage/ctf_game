import React, { useState } from "react";
import BackendURL from "../BackendURL";
import '../styles/Login.css'
import { useNavigate } from "react-router-dom";


const setRoleCookie = (ROLE) => {
    const expires = new Date(Date.now() + 86400 * 1000).toUTCString();
    document.cookie = `role=${ROLE}; expires=${expires}; path=/; domain=.ctfgame.pl; SameSite=Lax`;
};

const loginRequest = async ({ backendUrl, loginData }) => {
    try {
        const response = await fetch(`http://${backendUrl}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Instance-Id': localStorage.getItem('instanceId'),
            },
            body: JSON.stringify(loginData)
        });

        if (response.ok) {
            const data = await response.json();
            const {token, expiresIn} = data;
            sessionStorage.setItem('jwtToken', token);
            if (loginData.username === 'admin') {
                setRoleCookie("ROLE_ADMIN");
            } else {
                setRoleCookie("ROLE_USER");
            }
            return true;

        } else {
            console.error('Failed to login. Status:', response.status);
        }
    } catch (error) {
        console.error('Error while loggin in', error);
    }
    return false;
};

const Login = ({ backendUrl }) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();
    const [loginError, setLoginError] = useState(false);

    const handleLogin = async () => {
        const loginData = { username, password };
        const success = await loginRequest({ backendUrl, loginData });
        
        if (success) {
            setLoginError(false);
            navigate('/posts');
        } else {
            setLoginError(true);
        }
    };

    return (
        <div className="login-container">
            <div className="login-field">
                <div className="login-header">Log in</div>
                {loginError && (
                    <div className="login-data-alert">Username or password incorrect</div>
                )}
                <div className="login-fields">
                    <input
                        className="login-username"
                        placeholder="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                    <input
                        className="login-password"
                        type="password"
                        placeholder="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>
                <button className="login-submit" onClick={handleLogin}>
                    Submit
                </button>
            </div>
        </div>
    );
}

export default Login;