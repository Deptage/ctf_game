import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/messenger.css';
import BackendURL from '../BackendURL';

const fetchContacts = async (navigate) => {
  try {
    const backend = await BackendURL();
    const response = await fetch(`http://${backend}/conversation`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${sessionStorage.getItem('token')}`,
        'Instance-Id': localStorage.getItem('instanceId'),
      }
    });
    
    if (!response.ok) {
      throw new Error('Failed to fetch contacts');
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error fetching contacts:', error);
    sessionStorage.removeItem('token');
    navigate('/login');
    return [];
  }
}

function Contacts({ onContactClick, onLogout, useButtons, setUseButtons }) {
  const [contacts, setContacts] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetchContacts(navigate).then(data => setContacts(data || []));
  }, [navigate]);

  return (
    <div className="sidebar">
      <div className="sidebar-contacts">
        <h2>Contacts</h2>
        <ul>
          {contacts.length > 0 ? (
            contacts.map((contact) => (
              <li
                key={contact.id}
                className="contactItem"
                onClick={() => onContactClick(contact)}
              >
                {contact.contactName}
              </li>
            ))
          ) : (
            <li>Loading...</li>
          )}
        </ul>
      </div>
      <div className="sidebar-logout">
        <button className="logoutButton" onClick={onLogout}>Logout</button>
      </div>
    </div>
  );
}

export default Contacts;