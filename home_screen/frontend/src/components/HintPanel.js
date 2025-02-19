import React, { useEffect, useState } from "react";
import '../styles/HintPanel.css'
const HintPanel = ({ hintapp, flag }) => {

    const [allHints, setAllHints] = useState([]);
    const [revealState, setRevealState] = useState({});
    const [allowReveal, setAllowReveal] = useState([]);
    const request_hints = async () => {
        const token = localStorage.getItem('token');
        try {
            const response = await fetch(`http://localhost:8080/hints/flag/${flag}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            if (response.ok) {
                const result = await response.json();
                setAllHints(result);

                let tempAllowReveal = new Array(result.length).fill(false);
                for (let i = 0; i < result.length; i++) {
                    if (result[i].content === null) {
                        tempAllowReveal[i] = true;
                        setAllowReveal(tempAllowReveal);
                        break;
                    }
                }
            }
        } catch (error) {
            console.error("Error: ", error);
        }
    }

    const reveal_hint = async () => {
        const token = localStorage.getItem('token');
        try {
            const response = await fetch(`http://localhost:8080/hints/flag/${flag}/reveal`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            if (response.ok) {
                const result = await response.json();
                await request_hints();
            }
        } catch (error) {
            console.error("Error: ", error);
        }
    }

    const handleRevealClick = (index) => {
        setRevealState((prev) => ({ ...prev, [index]: true }));
    };

    const handleCancelReveal = (index) => {
        setRevealState((prev) => ({ ...prev, [index]: false }));
    };
    const HintWarning = ({ onConfirm, onCancel, index }) => {
        return (
            <div className="hint-warning">
                <p>Revealing this hint will deduct <b>[{allHints[index].pointsCost}]</b> points from your final score. Do you want to proceed?</p>
                <button className="confirm-button" onClick={onConfirm}>Yes</button>
                <button className="cancel-button" onClick={onCancel}>No</button>
            </div>
        );
    };

    useEffect(() => {
        request_hints();
    }, [])
    
        return (
            <div className="hint-container">
                <p className="hintgroup-name">Hints for {hintapp}</p>
                <div className="hints-list">
                    {allHints.length > 0 ? (
                        allHints
                            .map((hint, index) => (
                                <div key={index} className="hint-item">
                                    <h3 className="hint-name">{"Hint #"}{hint.ordinal}</h3>
                                    {hint.content !== null && <p className="hint-content">{hint.content}</p>}
    
                                    {hint.content === null && !revealState[index] && allowReveal[index]===true &&(
                                        <button
                                            className="hint-reveal-button"
                                            onClick={() => handleRevealClick(index)}
                                        >
                                            Reveal!
                                        </button>
                                    )}
                                    {hint.content === null && !revealState[index] && allowReveal[index]===false &&(
                                        <button
                                            className="hint-reveal-button hintable"
                                        >
                                        <span style={{ fontSize: '16px', color: 'white' }}>&#128274;</span>
                                        </button>
                                    )}
                                    {hint.content === null && revealState[index] && (
                                        <HintWarning
                                            onConfirm={() => reveal_hint(hintapp, hint.ordinal)}
                                            onCancel={() => handleCancelReveal(index)}
                                            index={index}
                                        />
                                    )}
                                </div>
                            ))
                    ) : (
                        <div className="hint-item">
                            <p className="hint-content">No hints available</p>
                        </div>
                       
                    )}
                </div>
            </div>
        );
    

}
export default HintPanel;