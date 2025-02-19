import React from "react"
import "./Header.css"
import { useNavigate } from 'react-router-dom';
import {ReactComponent as Logo} from "../logo.svg";

function Header(){
  const navigate = useNavigate();
  const handleClick = () => {
    navigate('/');
  };
  const handleCollabClick = () =>{
    document.body.classList.add('fade-out')
    setTimeout(()=>{
      navigate('/get-to-know-us');
      document.body.classList.remove('fade-out');
  }, 500);
  }
  return(
    <header className="header">
      <div className="header-content">
        <Logo className="logo clickable"  onClick={handleClick}/> {/* Logo SVG */}
        <h1 className="clickable" onClick={handleClick}>Duck Corporation Studios</h1>
        <div className="items">
          <h4 className="clickable" onClick={handleCollabClick}>Cooperation</h4>
        </div>
      </div>
    </header>
      );
}
export default Header;
