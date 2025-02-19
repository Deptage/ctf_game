import React from "react";

const Logout = ({ handleLogout }) => {
    return (
        <button onClick={handleLogout}
            style={{
                position: 'fixed',
                bottom: '60px',
                right: '10px',
                zIndex: 3,
                backgroundColor: '#F00',
                color: '#FFF',
                padding: '5px',
                borderRadius: '5px',
                fontFamily: 'monospace',
                fontSize: '1.2rem',
            }}>
            Logout
        </button>
    );
};

export default Logout;