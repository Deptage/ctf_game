import React from 'react';
import './BankFooter.css';
import Investments from './bankpage_body_components/Investments';


function Footer() {
  return (
    <footer className="App-footer">
      <Investments/>
      <p>&copy; 2024 SIGSEGV government property. Hacking is strictly forbidden!</p>
    </footer>
  );
}

export default Footer;
