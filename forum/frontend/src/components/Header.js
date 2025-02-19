import React from "react";
import { Link } from 'react-router-dom';
import '../styles/Header.css'
import { useNavigate } from "react-router-dom";

const Header = ({}) => {
    const navigate = useNavigate();

    const handleClick = () => {
        navigate(`/posts/`);
    };
    return (
        <>
            <div className="header">
                <div className="logo" onClick={handleClick}>
                    <div className="logo-icon">
                        <span className="letter">/</span>
                        <span className="letter">&gt;</span>
                    </div>
                    <span className="logo-text">Hack Info .RL</span>
                </div>
                <div className="nav">
                    <span className="nav-text">The Biggest Forum in Reverseland</span>
                    <div className="nav-links">
                        <Link to="/posts" className="nav-link active">Hacking and more - all posts</Link>
                        <Link to="/login" className="nav-link login">Log in</Link>
                    </div>
                </div>
            </div>
            <div className="second-border"></div>
        </>
    );
}

export default Header;