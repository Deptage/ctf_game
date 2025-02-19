import React, { useEffect, useState } from "react";
import '../styles/WikiVulnerabilities.css';
import vulnerabilitiesData from '../assets/files/vulnerabilities.json';
const Vulnerabilities = () =>{
    const [vulnerabilities, setVulnerabilities] = useState([]);

    useEffect(() => {
        setVulnerabilities(vulnerabilitiesData);
    }, []);
    return(
        <>
            {vulnerabilities.map((vulnerability, index) => (
                <div className="vulnerability-panel" key={index}>
                    <h2 className="vulnerability-text">{vulnerability.name}</h2>
                    <p className="vulnerability-text">{vulnerability.content}</p>
                </div>
            ))}
        </>
    );
}
export default Vulnerabilities;