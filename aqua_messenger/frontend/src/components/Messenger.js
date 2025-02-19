import React, { useState, useRef, useEffect } from 'react';
import Message from './Message';
import ResButton from './ResButton';
import '../styles/messenger.css';
import BackendURL from '../BackendURL';

function Messenger({ checkScriptedOrFree, bot_id, messages = [], setMessages, selectedContact, useButtons, headerRef }) {
  const [input, setInput] = useState('');
  //const possibleMessages = ['czesc mam na imie Antiqua Scarlet a ty?', 'k16', 'nie wstawie tutaj tej ikonki ale wiesz co chcę wysłać'];

  const [possibleMessages, setPossibleMessages] = useState([]);


  const chatHeaderRef = useRef(null);
  const inputContainerRef = useRef(null);
  const chatWindowRef = useRef(null);

  const [chatWindowHeight, setChatWindowHeight] = useState(0);
  const [currentUser, setCurrentUser] = useState(null);

  const updateChatWindowHeight = () => {
    const headerHeight = headerRef.current.offsetHeight || 0;
    const chatHeaderHeight = chatHeaderRef.current.offsetHeight || 0;
    const inputContainerHeight = inputContainerRef.current.offsetHeight || 0;
    const viewportHeight = window.innerHeight;

    const newHeight = viewportHeight - headerHeight - chatHeaderHeight - inputContainerHeight;

    // console.log("---- Chat Window Height Debug ----");
    // console.log(`Header Height: ${headerHeight}px`);
    // console.log(`Chat Header Height: ${chatHeaderHeight}px`);
    // console.log(`Input Container Height: ${inputContainerHeight}px`);
    // console.log(`Viewport Height: ${viewportHeight}px`);
    // console.log(`Calculated Chat Window Height: ${newHeight}px`);
    // console.log("---------------------------------");

    setChatWindowHeight(newHeight);
  };
  const scrollToBottom = () => {
    if (chatWindowRef.current) {
      chatWindowRef.current.scrollTop = chatWindowRef.current.scrollHeight;
    }
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const fetchCurrentUser = async () => {
    try {
      const backend = await BackendURL();
      const response = await fetch(`http://${backend}/auth/me`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${sessionStorage.getItem('token')}`,
          'Instance-Id': localStorage.getItem('instanceId'),
        },
      });

      if (!response.ok) {
        throw new Error(`Failed to fetch current user: ${response.statusText}`);
      }

      const data = await response.json();
      return data.username;
    } catch (error) {
      console.error('Error fetching current user:', error);
      return null;
    }
  };

  useEffect(() => {
    const getCurrentUser = async () => {
      const username = await fetchCurrentUser();
      setCurrentUser(username);
    };

    getCurrentUser();
  }, []);

  // useEffect(() => {
  //   updateChatWindowHeight();
  //   window.addEventListener('resize', updateChatWindowHeight);

  //   return () => {
  //     window.removeEventListener('resize', updateChatWindowHeight);
  //   };
  // }, [headerRef]);

  useEffect(() => {
    if (bot_id) {
      checkPossibleMessages();
    }
  }, [bot_id, messages]);
  
  const fetchMessagesMess = async () => {
    const backend = await BackendURL();
    fetch(`http://${backend}/conversation/${bot_id}/sentMessages`, {
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

  const handleSendFree = async (messageContent) => {
    //if (messageContent.trim()) {
      const backend = await BackendURL();

      fetch(`http://${backend}/conversation/${bot_id}/sendNew`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${sessionStorage.getItem('token')}`,
          'Instance-Id': localStorage.getItem('instanceId'),
        },
        body: JSON.stringify({
          content: messageContent
        }),
      })
        .then((response) => response.json())
        .then((data) => {
          console.log(data);
          if (data.status !== 'OK') {
            throw new Error('Response status is not OK');
          }
        })
        .catch((error) => {
          console.error('Error:', error);
        });
        
      await checkPossibleMessages();
      await fetchMessagesMess();
      await checkScriptedOrFree(bot_id);
      setInput('');
    //}
  };

  const sendScriptedMessage = async (message) => {
    try {
      const backend = await BackendURL();
      const response = await fetch(`http://${backend}/conversation/${bot_id}/sendExisting/${message.id}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${sessionStorage.getItem('token')}`,
          'Instance-Id': localStorage.getItem('instanceId'),
        },
      });

      if (!response.ok) {
        throw new Error(`Failed to send a possible message: ${response.statusText}`);
      }
      await checkPossibleMessages();
      await fetchMessagesMess();
      await checkScriptedOrFree(bot_id);
    } catch (error) {
      console.error('Error sending a possible message:', error);
    }
  };

  const checkPossibleMessages = async () => {
    try {
      const backend = await BackendURL();
      const response = await fetch(`http://${backend}/conversation/${bot_id}/nextMessages`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${sessionStorage.getItem('token')}`,
          'Instance-Id': localStorage.getItem('instanceId'),
        },
      });

      if (!response.ok) {
        throw new Error(`Failed to fetch possible messages: ${response.statusText}`);
      }

      const data = await response.json();
      setPossibleMessages(data);
    } catch (error) {
      console.error('Error fetching possible messages:', error);
      setPossibleMessages([]);
    }
  };


  return (
    <div className="messengerContainer">
      <div className="chatHeader" ref={chatHeaderRef}>
        {selectedContact ? (
          <h2>Chat with {selectedContact}</h2>
        ) : (
          <h2>Select a contact to start chatting</h2>
        )}
      </div>
      <div className="chatWindow" ref={chatWindowRef} style={{ maxHeight: `${chatWindowHeight - 50}px` }}>
        {messages.length > 0 ? (
          messages.map((message,index) => (
            <Message
              key={index}
              text={message.content}
              sender={message.from}
              currentUser={currentUser}
            />
          ))
        ) : (
          <p className="noMessages">No messages yet. Start the conversation!</p>
        )}
      </div>
      <div className="inputContainer" ref={inputContainerRef}>
        {selectedContact ? (
          useButtons ? (
            <div className="buttonOptions">
              {possibleMessages.map((msg) => (
                <ResButton
                  key={msg.id}
                  message={msg.content}
                  onClick={() => sendScriptedMessage(msg)}
                />
              ))}
            </div>
          ) : (
            <input
              className="input"
              type="text"
              placeholder="Type a message..."
              value={input}
              onChange={(e) => setInput(e.target.value)}
              onKeyPress={(e) => e.key === 'Enter' && handleSendFree(input)}
              maxLength={255}
            />
          )
        ) : (
          <input
            className="disabledInput"
            type="text"
            placeholder="Select a contact to start chatting"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyPress={(e) => e.key === 'Enter' && handleSendFree(input)}
            disabled={true}
          />
        )}
        {selectedContact && !useButtons && (
          <button className="button" onClick={() => handleSendFree(input)}>
            Send
          </button>
        )}
      </div>
    </div>
  );
}

export default Messenger;
