import React, { useState } from 'react';
import './BankHeader.css';
import bankLogo from './sigsegv_logo.svg';
import { useNavigate } from 'react-router-dom';
function Header({tokenPresent, username}) {
  const navigate = useNavigate();
  const handleClick = () => {
    navigate('/');
  };
  const handleRegisterClick = () => {
    navigate('/register');
  };
  const handleUsernameClick = () => {
    navigate('/account');
  }
  const handleLogoutClick = () => {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('username');
    sessionStorage.removeItem('firstAccountCreated');
    navigate('/');
    window.location.reload();
  }
  return (
    <header className="App-header" >
      <img src={bankLogo} alt="Bank Logo" className="bank-logo" onClick={handleClick}/>
      <h1 onClick={handleClick} className="clickable">SIGSEGV National Bank</h1>
      <div className="items">
        <h4>Investments</h4>
        <h4>Loan offers</h4>
        {!tokenPresent &&<h4 className="clickable" onClick={handleRegisterClick}>Register account</h4>}
        {tokenPresent && <h4 className="clickable" onClick={handleUsernameClick}>{username}</h4>}
        {tokenPresent && <h4 className="clickable" onClick={handleLogoutClick}>logout</h4>}
      </div> 
    </header>
  );
}

export default Header;