import React, { useState, useEffect } from 'react';
import '../styles/WikiPage.css'
import Burger from '../assets/icons/burger';
import { useNavigate } from 'react-router-dom';
import Vulnerabilities from './WikiVulnerabilities';
import Hints from './WikiHints';
import Authors from './WikiAuthors';
import SpoilerWarning from './WikiSpoilerWarning';
const WikiPage = () => {

    const [isNavOpen, setIsNavOpen] = useState(true);
    const [selectedContent, setSelectedContent] = useState('');
    const [tokenPresent, setTokenPresent] = useState(false);
    const toggleNavigation = () => {
        setIsNavOpen(!isNavOpen);
    }

    const navigate = useNavigate();

    const goToHome = () => {
        navigate('/');
    }
    const handleNavItemClick = (item) => {
        setSelectedContent(item);
    }

    const renderSelectedContent = () => {
        switch (selectedContent) {
            case 'Vulnerabilities':
                return <Vulnerabilities/>
            case 'Hints':
                return <Hints/>;
            case 'Authors':
                return <Authors />;
            default:
                return <p>Select an item from the navigation to see content</p>;
        }
    }
    useEffect(()=>{
        const token = localStorage.getItem('token');
        if (token) {
            setTokenPresent(true);
        }
    },[]);
    return (
        <div className="page">
            <div className="wikiBar">
                <Burger className="burger" color="#11ff55" onClick={toggleNavigation} />
                <h1>The Greatest Hacking Mission Wiki</h1>
                <div />
            </div>
            <div className="horizontalContent">
                <div className={`navigationList ${isNavOpen ? 'open' : 'closed'}`}>
                    <ul className="navItems">
                        <li><div className="navItemBox" onClick={() => handleNavItemClick('Vulnerabilities')}>Vulnerabilities</div></li>
                        {tokenPresent && <li><div className="navItemBox" onClick={() => handleNavItemClick('Hints')}>Hints</div></li>}
                        {!tokenPresent && <li><div className="navItemBox">Log in to unlock hints</div></li>}
                        <li><div className="navItemBox" onClick={() => handleNavItemClick('Authors')}>Authors</div></li>
                    </ul>
                    <div className="navFooter">
                        <button className="homeButton" onClick={goToHome}>Back to Home Screen</button>
                    </div>
                </div>
                <div className={`selectedContent ${isNavOpen ? 'shrink' : 'expand'}`}>
                    {renderSelectedContent()}
                </div>
            </div>
        </div>
    );
}
export default WikiPage;