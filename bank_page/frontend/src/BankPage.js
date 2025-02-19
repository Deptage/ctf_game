import React, { useState, useEffect } from 'react';
import './BankPage.css';
import Header from './components/BankHeader';
import Body from './components/BankBody';
import Footer from './components/BankFooter';


function BankPage() {
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


  return (
    <div className="App">
      <Header tokenPresent={tokenPresent} username={username}/>
      <Body/>
      <Footer/>

    </div>
  );
} 

export default BankPage;
