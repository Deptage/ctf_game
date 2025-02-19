import React, { useState } from "react";
import { useNavigate } from 'react-router-dom';
import '../styles/Login.css';

function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errorVisible, setErrorVisible] = useState(false);
  const navigate = useNavigate();

  const isEmail = (email) =>
    /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(email);
  
  const handleLogin = async (event) => {
    event.preventDefault();
    if (username && password) {
      if (!isEmail(username)) {
        setErrorVisible(true);
        return;
      }
      try {
        const res = await fetch('http://api.home.ctfgame.pl/auth/login', {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ username, password }),
        });
        if (res.ok) {
          const data = await res.json();
          localStorage.setItem("token", data.token);
          localStorage.setItem("username", username);
          navigate("/");
        } else {
          setErrorVisible(true);
        }
      } catch (error) {
        console.error("LOGIN ERROR :(((", error);
        setErrorVisible(true);
      }
    } else {
      setErrorVisible(true);
    }
  };


  const handleSignupClick = () => {
    navigate('/signup');
  };


  return (
    <div className="loginPage">
      <h1>The Greatest Hacking Mission</h1>
      <h2>Log in</h2>
      <div className="login-container">
        <p style={{ display: errorVisible ? 'block' : 'none', color: 'red' }}>
          E-mail or password incorrect
        </p>
        <form onSubmit={handleLogin}>
          <input
            type="text"
            placeholder="E-mail"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <button type="submit">Login</button>
        </form>
        <button
          onClick={handleSignupClick}
          style={{
            display: 'inline-block',
            marginTop: '20px',
            padding: '10px 20px',
            backgroundColor: '#007BFF',
            color: 'white',
            border: 'none',
            borderRadius: '5px',
            cursor: 'pointer'
          }}
        >
          Don't have an account? Sign up
        </button>
      </div>
    </div>
  );
}

export default Login;
