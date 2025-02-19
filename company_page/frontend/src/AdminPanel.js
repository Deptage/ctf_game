import React, { useState, useEffect } from "react";
import "./AdminPanel.css";
import BackendURL from "./BackendURL";

const AdminPanel = () =>{
    const [login,setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [ainfo, setAinfo] = useState('')
    const [responseMessage, setResponseMessage] = useState('');

    const onLoginChange = (event) => {
        setLogin(event.target.value);
    }
    const onPasswordChange = (event) => {
        setPassword(event.target.value);
    }
    const onAinfoChange = (event) =>{
        setAinfo(event.target.value);
    }

    const handleSubmit = async (event) => {
        event.preventDefault();
        const data = { login, password, ainfo };
        const backendurl = BackendURL();

        try {
            const response = await fetch(`http://${backendurl}/adminPanel`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Instance-Id': localStorage.getItem('instanceId'),
                },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                const result = await response.text();
                setLogin('');
                setPassword('');
                setAinfo('');
                console.log(result);
                setResponseMessage(`${result}`);
            } else {
                setResponseMessage(`Error: ${response.statusText}`);
            }
        } catch (error) {
            setResponseMessage(`Error:${error.message}`);
            
        }
    }


    return(
        <div className="adm">
            <h1 className="admtitle">Duck Corporation Studios Test Admin Panel</h1>
            <form className="admform"  onSubmit={handleSubmit}>
                        ADMIN LOGIN:
                        <input type="text" value={login} onChange={onLoginChange} placeholder="Login" required/>

                        ADMIN PASSWORD:
                        <input type="password" value={password} onChange={onPasswordChange} placeholder="Password" required/>

                        ADDITIONAL INFO:
                        <input type="text" value={ainfo} onChange={onAinfoChange} placeholder="Additional info" required />
                <button type="submit" className="confirmButton">Submit</button>
                {responseMessage && <p className="responseMessage">{responseMessage}</p>}
            </form>
        </div>
        
    )
}
export default AdminPanel;