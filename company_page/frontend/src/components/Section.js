import React from "react";
import "./Section.css";

const Section = ({title, paragraph, number, end}) => {
    return(
        <div className={`section ${end ? "end" : ""}`}>
            <div className="textContent">
                <h1>
                    {title}
                </h1>
                <p>
                    {paragraph}
                </p>
            </div>
            <div className="lineContainer">
                <div className="line"></div>
                <div className="circle">
                <span>{number}</span>
                </div>
                {!end && <div className="line"></div>}
            </div>
        </div>
    );
}
export default Section;