import React, { forwardRef } from 'react';
import '../styles/messenger.css';
import logo from '../assets/aqua_messenger_icon.png'

const AquaHeader = forwardRef((props, ref) => (
    <header className="headerContainer" ref={ref}>
        <div className="headerContent">
            <img src={logo} alt="Messenger Logo" className="logo" />
            <h1>Aqua Messenger</h1>
        </div>
    </header>
));

export default AquaHeader;