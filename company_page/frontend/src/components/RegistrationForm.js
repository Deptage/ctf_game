import React, { useState, useEffect } from "react";
import './RegistrationForm.css';
function RegistrationForm(){
    const [name,setName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [reason,setReason] = useState('');
    const [charsLeft, setCharsLeft] = useState(1000);

    const nameChange = (event) => {
        setName(event.target.value);
    };
    const lastNameChange = (event) => {
        setLastName(event.target.value);
    };
    const emailChange = (event) => {
        setEmail(event.target.value);
    };
    const reasonChange = (event) => {
        setReason(event.target.value);
        setCharsLeft(1000 - event.target.value.length);
    };


    const handleSubmit = (e) => {
        e.preventDefault();
        setName('');
        setLastName('');
        setEmail('');
        setReason('');
        setCharsLeft(1000);
    }
    return(
        <div className="registrationForm">
            <form className="form">
                <div className="textForm">
                    <h2>Contact us here!</h2>
                </div>
                <div className="textForm">
                    Name:
                    <input type="text" value={name} onChange={nameChange} placeholder="Name" required maxLength={50}/>
                </div>
                <div className="textForm">
                    Last name:
                    <input type="text" value={lastName} onChange={lastNameChange} placeholder="Last name" required maxLength={50}/>
                </div>
                <div className="textForm">
                    E-mail address:
                    <input type="text" value={email} onChange={emailChange} placeholder="E-mail address" required maxLength={100} />
                </div>
                <div className="textForm">
                    Why do you want to contact us?:
                    <textarea type="text" value={reason} onChange={reasonChange} placeholder="Reason" required maxLength={1000}/>
                    <small>{charsLeft} characters left</small>
                </div>
                <button type="submit" className="confirmButton">Submit form</button>
                
                
            </form>
        </div>
    );
};

export default RegistrationForm;