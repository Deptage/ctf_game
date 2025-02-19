import React, { useEffect, useState, useRef } from 'react';
import Messenger from './components/Messenger';
import Contacts from './components/Contacts';
import './styles/messenger.css';
import AquaHeader from './components/AquaHeader';
import BackendURL from './BackendURL';
import Login from './components/Login';
import { BrowserRouter as Router, Routes, Route, Navigate, useParams } from 'react-router-dom';
import NoBackendPortError from './NoBackendPortError';

function App() {
  const [messages, setMessages] = useState([]);
  const [selectedContact, setSelectedContact] = useState('');
  const [authenticated, setAuthenticated] = useState(false);
  const [useButtons, setUseButtons] = useState(false);
  const headerRef = useRef(null);

  const fetchMessages = async (contact) => {
    const backend = await BackendURL();
    fetch(`http://${backend}/conversation/${contact.id}/sentMessages`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${sessionStorage.getItem('token')}`,
        'Instance-Id': localStorage.getItem('instanceId'),
      }
    })
      .then(response => response.json())
      .then(data => {
        setMessages(data);
      });
  };

  const handleContactClick = (contact) => {
    setSelectedContact(contact);
    fetchMessages(contact);
    checkScriptedOrFree(contact.id);
  };

  useEffect(() => {
    const token = sessionStorage.getItem("token");
    if (token) {
      setAuthenticated(true);
    } else {
      setAuthenticated(false);
    }
  }, []);

  const handleLogin = () => {
    setSelectedContact('');
    setMessages([]);
    setAuthenticated(true);
  };

  const handleLogout = () => {
    setAuthenticated(false);
    setSelectedContact('');
    setMessages([]);
    localStorage.removeItem('token');
  };
  const SavePortAndRedirect = () => {
    const { data } = useParams();
    // Decode the base64-encoded data and parse it as JSON
    const decodedData = JSON.parse(atob(data));
    const instanceId = decodedData.instance_id;
    if (!instanceId){
      instanceId = 'instance-id'
    }

    useEffect(() => {
      localStorage.setItem('instanceId', instanceId);
    }, [instanceId]);
    return <Navigate to="/login" />;
  };


  const checkScriptedOrFree = async (contact_id) => {
    try {
      const backend = await BackendURL();
      const response = await fetch(`http://${backend}/conversation/${contact_id}/info`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${sessionStorage.getItem('token')}`,
          'Instance-Id': localStorage.getItem('instanceId'),
        },
      });
  
      if (response.ok) {
        const data = await response.json();
        if(data.type === 'SCRIPTED'){
          setUseButtons(true);
        } else {
          setUseButtons(false);
        }
      } else {
        console.error(`Error fetching conversation info: ${response.status}`);
      }
    } catch (error) {
      console.error('Error in checkScriptedOrFree:', error);
    }
  };




  return (
    <Router>
      <div className="layout">
        <Routes>
          <Route path="/login" element={authenticated ? <Navigate to="/" /> : <Login onLogin={handleLogin} />} />
          <Route path="/" element={authenticated ? (
            <>
              <AquaHeader ref={headerRef}/>
              <div className="mainContent">
                <Contacts onContactClick={handleContactClick} onLogout={handleLogout} useButtons={useButtons} setUseButtons={setUseButtons} />
                <Messenger fetchMessages={fetchMessages} checkScriptedOrFree={checkScriptedOrFree} bot_id={selectedContact.id} messages={messages} setMessages={setMessages} selectedContact={selectedContact.contactName} useButtons={useButtons} headerRef={headerRef}/>
              </div>
            </>
          ) : <Navigate to="/login" />} />
          <Route path="/start/:data" element={<SavePortAndRedirect />} />
          <Route path="/error" element={<NoBackendPortError />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;