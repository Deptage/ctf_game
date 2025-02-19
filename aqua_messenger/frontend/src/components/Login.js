import React, {useEffect, useState} from 'react';
import '../styles/login.css';
import BackendURL from '../BackendURL';


function Login({onLogin}){
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorVisible, setErrorVisible] = useState(false);
    const [error, setError] = useState('');
   

    const handleLogin = async (event) => {
        event.preventDefault();
        if (username && password) {
            try {
                const url = BackendURL();
                const res = await fetch(`http://${url}/auth/login`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        "Instance-Id": localStorage.getItem("instanceId"),
                    },
                    body: JSON.stringify({ username, password }),
                });
                if (res.ok) {
                    
                    const data = await res.json();
                    
                    if (data.response === "Authentication error") {
                        setError("Invalid username or password.");
                        setErrorVisible(true);
                        return;
                    }
                    sessionStorage.setItem("token", data.token);
                    onLogin();
                } else {
                    setErrorVisible(true);
                }
            } catch (error) {
                setError("An error occurred. Please try again later.");
                setErrorVisible(true);
            }
        } else {
            setError("Please fill in all fields correctly to log in.");
            setErrorVisible(true);

        }
    };

    return (
        <div className="loginPage">
            <div className="loginCard">
                <h1>Aqua Messenger</h1>
                <h2>Log in</h2>
                <form onSubmit={handleLogin}>
                    <div className="inputGroup">
                        <input
                            type="text"
                            placeholder="Username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                        />
                        <input
                            type="password"
                            placeholder="Password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </div>
                    <button type="submit" className="loginButton">Login</button>
                    {errorVisible && (
                        <p className="errorMessage">
                            {error}
                        </p>
                    )}
                </form>
            </div>
        </div>
    );
}

export default Login;