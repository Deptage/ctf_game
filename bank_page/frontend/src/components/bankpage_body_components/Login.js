import React, { useState } from 'react';
import './Login.css';
import { Navigate, useNavigate } from 'react-router-dom';
import BackendURL from '../../BackendURL';

function Login() {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(false);
    const [message, setMessage] = useState('');
    const [token, setToken] = useState(null);
    const [existsWarning, setExistsWarning] = useState(true);
    const [wrongDataWarning, setWrongDataWarning] = useState(false);
    const [wrongData, setWrongData] = useState('');

    const navigate = useNavigate();
    const handleRegisterClick = () =>{
        navigate("/register");
    };
    const loginChange = (event) => {
        setLogin(event.target.value);
        setExistsWarning(true);
        setWrongDataWarning(false);
    };

    const passwordChange = (event) => {
        setPassword(event.target.value);
        setWrongDataWarning(false);
    };
    const check_availability_and_request_auth = async (username, password) => {
        const backendurl = await BackendURL();
        try{
            const response = await fetch(`http://${backendurl}/auth/exists?username=${username}`,{
                method: 'GET',
                headers:{
                    'Instance-Id': localStorage.getItem('instanceId'),
                }
            });
            if (response.ok){
                const result = await response.json();
                if(result.exists == true){
                    setExistsWarning(true);
                    await request_auth_login(username,password);
                }else{
                    setExistsWarning(false);
                }
                
            }else{
                console.log("wrong username");
            }
        }catch (error) {
            console.error('Error:', error);
        }
    }
    const request_auth_login = async (username, password) => {
        const data = {username, password,};
        const backendurl = await BackendURL();
        try{
            const response = await fetch(`http://${backendurl}/auth/login`,{
                method: 'POST',
                headers: {
                    'Content-Type':'application/json',
                    'Instance-Id': localStorage.getItem('instanceId'),
                },
                body: JSON.stringify(data),
            });
            if (response.ok) {
                const result = await response.json();
                setToken(result.token);
                setMessage('Login successful!');
                sessionStorage.setItem('token', result.token);
                sessionStorage.setItem('username', username);
                navigate('/account');
            } else if (response.status === 400) {
                setWrongData(true);
                setWrongDataWarning("Check username and password");
            } else if (response.status === 401) {
                setWrongData(true);
                setWrongDataWarning("Check username and password");
            } else {
                setMessage('error');
            }
        }catch (error) {
            console.error('Error:', error);
        }
    }
    const handleSubmit = (e) =>{
        e.preventDefault();
        check_availability_and_request_auth(login,password);
    };
    
    return (
        <div className="loginContainer">
            <h2 className="loginTitle">Login into your account</h2>
            <form className="form" onSubmit={handleSubmit}>
                <div className={`textForm ${error ? 'error' : ''}`}>
                    <label>
                        Login:
                        <input
                            className={error?'errorInput':''}
                            type="text"
                            value={login}
                            onChange={loginChange}
                            placeholder="Login"
                            required
                        />
                        {!existsWarning && <p className="exists-warning">Username doesn't exist!</p>}
                    </label>
                </div>
                <div className={`textForm ${error ? 'error' : ''}`}>
                    <label>
                        Password:
                        <input
                            className={error?'errorInput':''}
                            type="password"
                            value={password}
                            onChange={passwordChange}
                            placeholder="Password"
                            required
                        />
                        {wrongData && <p className="exists-warning">{wrongDataWarning}</p>}
                    </label>
                </div>
                {error && <p className="errorMessage">Incorrect login or password</p>}
                <button type="submit" className="loginButton">Login</button>
                <p>Don't have an account? <p className="registerText" onClick={handleRegisterClick}>Register</p></p>
            </form>
        </div>
    );
}

export default Login;