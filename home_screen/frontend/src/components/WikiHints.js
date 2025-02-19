import React from "react";
import '../styles/WikiHints.css';
import HintPanel from "./HintPanel";
const Hints = () => {
    return (
        <>
            <div className="hintPanels">
                <HintPanel hintapp="Company - flag 1" flag={1}/>
            </div>
            
            <div className="hintPanels">
                <HintPanel hintapp="Forum - flag 1" flag={5}/>
            </div>

            <div className="hintPanels">
                <HintPanel hintapp="Forum - flag 2" flag={6}/>
            </div>

            <div className="hintPanels">
                <HintPanel hintapp="Messenger - flag 1" flag={7}/>
            </div>

            <div className="hintPanels">
                <HintPanel hintapp="Messenger - flag 2" flag={8}/>
            </div>

            <div className="hintPanels">
                <HintPanel hintapp="Bank - flag 1" flag={2}/>
            </div>

            <div className="hintPanels">
                <HintPanel hintapp="Bank - flag 2" flag={3}/>
            </div>
            
            <div className="hintPanels">
                <HintPanel hintapp="Bank - flag 3" flag={4}/>
            </div>

            
        </>
    );
}
export default Hints;