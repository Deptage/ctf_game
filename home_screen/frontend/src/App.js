import React, { useState, useEffect } from 'react';
import MatrixBackground from './components/MatrixBackground';
import DraggableWindow from './components/DraggableWindow';
import Clock from './components/Clock';
import './styles/App.css';
import TodoList from './components/ToDoList';
import MailList from './components/Mail';
import LogoComponent from './components/LogoComp';
import CustomBrowser from './components/CustomBrowser';
import Login from './components/Login';
import Banner from './components/Banner';
import Signup from './components/Signup';
import Logout from './components/Logout';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import WikiPage from './components/WikiPage';
import MainPage from './components/MainPage';
import { useNavigate } from 'react-router-dom';
import AnimationButton from './components/AnimationButton';

function App() {
  const storedMatrixRunning = localStorage.getItem("isMatrixRunning");
  const [isMatrixRunning, setMatrixRunning] = useState(storedMatrixRunning === "false" ? false : true);
  const [username, setUsername] = useState('');
  const toggleMatrixAnimation = () => {
    setMatrixRunning((prev) => !prev);
  };

  useEffect(() => {
    localStorage.setItem("isMatrixRunning", isMatrixRunning.toString());
  }, [isMatrixRunning]);

  const fetchUserMe = async () => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const res = await fetch(`http://api.home.ctfgame.pl/users/me`, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });
        if (res.ok) {
          const result = await res.json();
          setUsername(result.username);
          return true;
        } else {
          const errorData = await res.json();
          switch (res.status) {
            case 403:
              console.error('403 Forbidden: The user is not authenticated.');
              break;
            case 401:
              console.error('401 Unauthorized: JWT token is invalid.');
              break;
            case 400:
              console.error('400 Bad Request: Invalid form data.');
              break;
          }
          localStorage.removeItem("token");
          
          return false;
        }
      } catch (error) {
        console.error("jakis inny error!!!");
      }
    }
    else {
      return false;
    }
  };

  return (
    <div className="App">
      <MatrixBackground paused={isMatrixRunning}/>
      <Router>
        <Routes>
          <Route
            path="/login"
            element={
              <Login />
            } />
          <Route
            path="/signup"
            element={
              <Signup />
            }
          />
          <Route
            path="/wiki"
            element={
              <WikiPage/>
            }
          />
          <Route
            path="/"
            element={
              <MainPage fetchUserMe={fetchUserMe} setMatrixRunning={setMatrixRunning} username={username}/>
            }
          />

        </Routes>
      </Router>
      <AnimationButton
                toggleMatrixAnimation={toggleMatrixAnimation}
      />
    </div >
  );
}

export default App;
