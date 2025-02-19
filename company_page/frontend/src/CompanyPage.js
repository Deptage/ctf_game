import React, { useState, useEffect } from "react";
import "./CompanyPage.css";
import Section from "./components/Section";
import Footer from "./components/Footer";
import Header from "./components/Header"
import { useNavigate } from 'react-router-dom';

function CompanyPage() {
    const navigate = useNavigate();
    const handleClick = () => {
        document.body.classList.add('fade-out')
        setTimeout(()=>{
            navigate('/get-to-know-us');
            document.body.classList.remove('fade-out');
        }, 500);
        
    };
    return (
        <div className="App">
            <Header />
            <div className="content">
                <div className="row">
                    <div className="section-container">
                        <Section title="Duck Corporation Studios AG" paragraph="Music production since 2014 with highest standards" number={1} />
                    </div>
                    <div className="section-container">
                        <Section title="Our services" paragraph="Recording, publishing, design, marketing - it's all us!" number={2} />
                    </div>

                </div>
                <div className="row">
                    <div className="section-container">
                        <Section title="Succeed with us!" paragraph="Like our first composer A3S with hits like Programmers' Ballad and Segmentation Fault" number={3} />
                    </div>                   
                    <div className="section-container clickable-alt" onClick={handleClick}>
                        <Section title="We offer long-lasting and stable partnership!" paragraph="Get to know us here!" number={4} end={true} />
                    </div>
                </div>
                
            </div>
            <Footer />
        </div>
    );
}

export default CompanyPage;