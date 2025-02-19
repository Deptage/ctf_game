import React, { useState } from 'react';
import styled from 'styled-components';
import DraggableWindow from './DraggableWindow';
import '../styles/DraggableWindow.css';
import logo from '../assets/images/aqua_messenger_icon.png';

const Logo = styled.img`
  width: 120px;
  transition: transform 0.3s ease, filter 0.3s ease;

  &:hover {
    transform: scale(1.1);
    filter: drop-shadow(0 0 10px rgba(0, 0, 0, 0.2));
  }
`;

const LogoContainer = styled.div`
  position: absolute;
  top: ${({ defaultPosition }) => defaultPosition.y}px;
  left: ${({ defaultPosition }) => defaultPosition.x}px;
  display: flex;
  flex-direction: column; /* Stack logo and text vertically */
  align-items: center;
  z-index: 1;
`;

const LogoText = styled.p`
  margin-top: 10px; /* Space between the logo and text */
  font-size: 16px;
  color: #FFF; /* Adjust color as needed */
  text-align: center;
`;

const LogoComponent = ({ defaultPositionPercent, text, startInstance }) => {
  const [showAlertWindow, setShowAlertWindow] = useState(false);
  const [confirmationMessage, setConfirmationMessage] = useState('');

  const calculateDefaultPosition = () => {
    const x = (window.innerWidth * defaultPositionPercent.x) / 100;
    const y = (window.innerHeight * defaultPositionPercent.y) / 110;
    return { x, y };
  };

  const handleLogoClick = () => {
    setShowAlertWindow(true);
  };

  const handleYesClick = () => {
    startInstance('Messenger');
    setShowAlertWindow(false);
  };

  const handleNoClick = () => {
    setConfirmationMessage('Maszyna wirtualna NIE zostanie włączona.');
    setTimeout(() => {
      setShowAlertWindow(false);
    }, 1000);
  };

  return (
    <>
      <LogoContainer defaultPosition={calculateDefaultPosition()}>
        <Logo src={logo} alt="Logo" onClick={handleLogoClick} />
        <LogoText>{text}</LogoText>
      </LogoContainer>
      {showAlertWindow && (
        <DraggableWindow defaultPositionPercent={{ x: 50, y: 50 }} style={{ zIndex: 100 }}>
          <div style={{ textAlign: 'center' }}>
            <p>A VM INSTANCE WILL BE RUN FOR AN HOUR. DO YOU CONFIRM?</p>
            <div style={{ margin: '20px 0' }}>
              <button onClick={handleYesClick} style={{ backgroundColor: 'green', padding: '10px', margin: '10px' }}>YES</button>
              <button onClick={handleNoClick} style={{ backgroundColor: 'red', padding: '10px', margin: '10px' }}>NO</button>
            </div>
            {confirmationMessage && <p>{confirmationMessage}</p>}
          </div>
        </DraggableWindow>
      )
      }
    </>
  );
};

export default LogoComponent;
