import React from "react";

const AnimationButton = ({ toggleMatrixAnimation }) => {
    return (
        <button
            onClick={toggleMatrixAnimation}
            style={{
                position: "fixed",
                top: "10px",
                right: "10px",
                zIndex: 10000,
                backgroundColor: "#F00",
                color: "#FFF",
                padding: "5px",
                borderRadius: "5px",
                fontFamily: "monospace",
                fontSize: "1.2rem",
                border: "none",
                cursor: "pointer",
            }}
        >
        Toggle Background Animation
        </button>
    );
};

export default AnimationButton;
