import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/CustomBrowser.css';

const ComponentBank = ({ startInstance, returnToMainPage }) => {
  
  const [confirmationMessage, setConfirmationMessage] = useState('');
  const handleYesClick = () => {
    startInstance('Bank', setConfirmationMessage);
    returnToMainPage();
  };
  const handleNoClick = () => {
    setConfirmationMessage('Maszyna wirtualna NIE zostanie włączona.');
    returnToMainPage();
  };
  return (
    <div className="component-container">
      <p>A VM INSTANCE WILL BE RUN FOR AN HOUR. DO YOU CONFIRM?</p>
      <div className="button-group">
        <button onClick={handleYesClick} className="button yes">YES</button>
        <button onClick={handleNoClick} className="button no">NO</button>
      </div>
      {confirmationMessage && <p>{confirmationMessage}</p>}
    </div>
  );
};

const ComponentForum = ({ startInstance, returnToMainPage }) => {
  const [confirmationMessage, setConfirmationMessage] = useState('');
  const handleYesClick = () => {
    startInstance('Forum', setConfirmationMessage);
    returnToMainPage();
  };
  const handleNoClick = () => {
    setConfirmationMessage('Maszyna wirtualna NIE zostanie włączona.');
    returnToMainPage();
  };
  return (
    <div className="component-container">
      <p>A VM INSTANCE WILL BE RUN FOR AN HOUR. DO YOU CONFIRM?</p>
      <div className="button-group">
        <button onClick={handleYesClick} className="button yes">YES</button>
        <button onClick={handleNoClick} className="button no">NO</button>
      </div>
      {confirmationMessage && <p>{confirmationMessage}</p>}
    </div>
  );
};

const ComponentFirma = ({ startInstance, returnToMainPage }) => {
  const [confirmationMessage, setConfirmationMessage] = useState('');
  const handleYesClick = () => {
    startInstance('Company', setConfirmationMessage);
    returnToMainPage();
  };
  const handleNoClick = () => {
    setConfirmationMessage('Maszyna wirtualna NIE zostanie włączona.');
    returnToMainPage();
  };
  return (
    <div className="component-container">
      <p>A VM INSTANCE WILL BE RUN FOR AN HOUR. DO YOU CONFIRM?</p>
      <div className="button-group">
        <button onClick={handleYesClick} className="button yes">YES</button>
        <button onClick={handleNoClick} className="button no">NO</button>
      </div>
      {confirmationMessage && <p>{confirmationMessage}</p>}
    </div>
  );
};

const Wskazowki = () => {
  const navigate=useNavigate();
  useEffect(()=>{
    navigate('/wiki');
  },[])
  
}

const CustomBrowser = ({ startInstance }) => {
  const [activeComponent, setActiveComponent] = useState(null);

  const returnToMainPage = () => {
    setActiveComponent(null);
  };

  const renderContent = () => {
    switch (activeComponent) {
      case 'ComponentBank':
        return <ComponentBank startInstance={startInstance} returnToMainPage={returnToMainPage} />;
      case 'ComponentForum':
        return <ComponentForum startInstance={startInstance} returnToMainPage={returnToMainPage} />;
      case 'ComponentFirma':
        return <ComponentFirma startInstance={startInstance} returnToMainPage={returnToMainPage} />;
      case 'Wskazowki':
        return <Wskazowki />;
      default:
        return (
          <div className="nav-area">
            <h3 className="bookmark-text">Bookmarks</h3>
            <div className="nav-icons-container">
              <div className="nav-icon" onClick={() => setActiveComponent('ComponentBank')}>Bank</div>
              <div className="nav-icon" onClick={() => setActiveComponent('ComponentForum')}>Forum</div>
              <div className="nav-icon" onClick={() => setActiveComponent('ComponentFirma')}>Company</div>
              <div className="nav-icon" onClick={() => setActiveComponent('Wskazowki')}>Wiki</div>
            </div>
          </div>
        );
    }
  };

  return (
    <div className="browser-container">
      <div className="browser-header">
        <h2>Powerful Browser</h2>
      </div>
      <div className="content-container">{renderContent()}</div>
    </div>
  );
};

export default CustomBrowser;