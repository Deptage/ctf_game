import React from "react";
import '../styles/WikiSpoilerWarning.css';

const SpoilerWarning = ({onContinue}) =>{
    return(
        <div className="spoilerAlert">
            <p>Potential spoilers ahead. Continue?</p>
            <div className="selection">
                <button className="yes" onClick={onContinue}>Continue</button>
            </div>
        </div>
    );
};
export default SpoilerWarning;