import React, {useState, useEffect} from 'react';
import './BankBody.css';
import Login from './bankpage_body_components/Login.js'
import Misc from './bankpage_body_components/Misc.js'
import Investments from './bankpage_body_components/Investments.js';
import DollarBackground from './DollarBackground.js';
import { useNavigate } from 'react-router-dom';
function Body() {
  const navigate = useNavigate();
  const handleClick = () => {
    navigate('/account');
  }
  const [tokenPresent, setTokenPresent] = useState(false);
  const [username, setUsername] = useState('');

  useEffect(() => {
    const token = sessionStorage.getItem('token');
    if (token) {
      setTokenPresent(true);
      const storedUsername = sessionStorage.getItem('username');
      setUsername(storedUsername || '');
    }
  }, []);
  const gradientAngle = 90;
  const gradientColors = ['rgba(48,46,84,1)', 'rgba(183,187,143,1)', 'rgba(0,212,255,1)'];
  return (
    <main className="App-body">
        <DollarBackground multiplier={1} gradientAngle={gradientAngle} gradientColors={gradientColors}/>
        <div className="container">
            <div className="misc-section">
                <Misc />
            </div>
            <div className="login-section">
                {!tokenPresent &&<Login/>}
                {tokenPresent&& <h1 className="hello-text" onClick={handleClick}>Welcome, {username}</h1>}
            </div>
        </div>
         
    </main>
  );
}

export default Body;