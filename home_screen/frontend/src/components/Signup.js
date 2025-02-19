import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Signup.css';
import AnimationButton from './AnimationButton';
import policies from '../policies.js'

function Signup() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [errorVisible, setErrorVisible] = useState(false);
  const [errorVisible1, setErrorVisible1] = useState(false);
  const [errorVisible2, setErrorVisible2] = useState(false);
  const [loginMessage, setLoginMessage] = useState('');
  const [passwdMessage, setPasswdMessage] = useState('');
  const [failedLoginValidation, setFailedLoginValidation] = useState(false);
  const [failedPasswordValidation, setFailedPasswordValidation] = useState(false);
  const [formSubmitted, setFormSubmitted] = useState(false);
  const { passwdPolicy, loginPolicy } = policies();
  const [university, setUniversity] = useState('');
  const [signupsuccessful, setSignupsuccessful] = useState(false);
  const navigate = useNavigate();

  const universities = [
    { value: 'BTUCS', label: 'Brandenburgische Technische Universität Cottbus-Senftenberg' },
    { value: 'IPDVI', label: 'Instituto Politécnico de Viseu' },
    { value: 'KARLU', label: 'Karlstads Universitet' },
    { value: 'PUT', label: 'Poznań University of Technology' },
    { value: 'UDCAN', label: 'Universidad de Cantabria' },
    { value: 'UDCAT', label: 'Università di Catania' },
    { value: 'UDMON', label: 'Université de Mons' },
    { value: 'UPHDF', label: 'Université Polytechnique Hauts-de-France' },
    { value: 'UOTPE', label: 'University of the Peloponnese' },
    { value: 'VAASY', label: 'Vaasan Yliopisto' },
    { value: 'OTHER', label: 'Other or none' },
  ];

  const isEmail = (email) =>
    /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(email);

  const customOption = ({ data }) => (
    <div style={{ display: 'flex', alignItems: 'center' }}>
      <div style={{ marginRight: '10px' }}>{data.flag}</div>
      <span>{data.label}</span>
    </div>
  );

  const handleSignup = async (event) => {
    event.preventDefault();
    setErrorVisible(false);
    setErrorVisible1(false);
    setErrorVisible2(false);
    setSignupsuccessful(false);
    setFailedPasswordValidation(false);
    setFailedLoginValidation(false);
    setLoginMessage("");
    setPasswdMessage("");

    console.log("========");
    if (password !== confirmPassword) {
      setErrorVisible(true);
    }

    if (!isEmail(username)) {
      setErrorVisible2(true);
    }

    let login = username.split('@')[0];

    const loginMessageTemp = loginPolicy(login);
    const passwdMessageTemp = passwdPolicy(password);

    if (loginMessageTemp === "correct") {
      setFailedLoginValidation(false);
    } else {
      setFailedLoginValidation(true);
      setLoginMessage(loginMessageTemp);
    }

    setPasswdMessage(passwdPolicy(password));

    if (passwdMessageTemp === "correct") {
      setFailedPasswordValidation(false);
    } else {
      setFailedPasswordValidation(true);
      setPasswdMessage(passwdMessageTemp);
    }

    if(!isEmail(username) || loginMessageTemp !== "correct" || passwdMessageTemp !== "correct" || (password !== confirmPassword)){
      return;
    }else{
      setLoginMessage("");
      setPasswdMessage("");
      await submitSignup();
    }


  };

  const navigateToLogin = () => {
    setTimeout(() => {
      navigate('/login');
    }, 1000);
  };

  const handleLoginClick = () => {
    navigate('/login');
  };

  const submitSignup = async () => {
    try {
      const res = await fetch('http://api.home.ctfgame.pl/auth/signup', {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, password, university }),
      });

      if (res.ok) {
        setSignupsuccessful(true);
        navigateToLogin();
      } else {
        setErrorVisible1(true);
      }
    } catch (error) {
      console.error("SIGNUP ERROR :(((", error);
      setErrorVisible1(true);
    }
  };

  return (
    <div className="signupPage">
      <h1>The Greatest Hacking Mission</h1>
      <h2>Sign in</h2>
      <div className="signup-container">
        <p className="signup-warnings" style={{ display: errorVisible ? 'block' : 'none', color: 'red' }}>
          Passwords do not match
        </p>
        <p className="signup-warnings" style={{ display: errorVisible1 ? 'block' : 'none', color: 'red' }}>
          E-mail is already in use or account has been already registered on the machine
        </p>
        <p className="signup-warnings" style={{ display: errorVisible2 ? 'block' : 'none', color: 'red' }}>
          Invalid e-mail address
        </p>
        {failedLoginValidation && <p className="signup-warnings" style={{ display: 'block', color: 'red' }}>
          {loginMessage}
        </p>}
        {failedPasswordValidation && <p className="signup-warnings" style={{ display: 'block', color: 'red' }}>
          {passwdMessage}
        </p>}
        <p className="signup-successful" style={{ display: signupsuccessful ? 'block' : 'none' }}>Sign up successfull, redirecting to log in page...</p>
        <form onSubmit={handleSignup}>
          <input
            type="text"
            placeholder="E-mail"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <input
            type="password"
            placeholder="Confirm Password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
          />
          <select
            value={university}
            onChange={(e) => setUniversity(e.target.value)}
            required
          >
            <option value="" disabled>Select your university</option>
            {universities.map((uni) => (
              <option key={uni.value} value={uni.value}>
                {uni.label}
              </option>
            ))}
          </select>
          <button type="submit">Sign Up</button>
        </form>
        <button
          onClick={handleLoginClick}
          style={{
            display: 'inline-block',
            marginTop: '20px',
            padding: '10px 20px',
            backgroundColor: '#007BFF',
            color: 'white',
            border: 'none',
            borderRadius: '5px',
            cursor: 'pointer'
          }}
        >
          You already have an account? Log in.
        </button>
      </div>
    </div>
  );
}

export default Signup;
