import React from 'react';
import '../styles/messenger.css';

function Message({ text, sender, currentUser }) {
    const messageClass = sender === currentUser ? 'messageSent' : 'messageReceived';

    return (
        <div className={messageClass}>
            <div className="sender">{sender}</div>
            <span dangerouslySetInnerHTML={{ __html: text }} />
        </div>
    );
}

export default Message;