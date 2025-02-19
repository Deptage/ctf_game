import React, { useEffect, useState } from "react";

const Points = ({username}) => {
    const [points, setPoints] = useState(0);
    useEffect(() => {
        const fetchPoints = async () => {
            try {
                const response = await fetch("https://put-ctf-competition.put.poznan.pl/?rest_route=/score/v1/data", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({
                        username: localStorage.getItem("username"),
                    }),
                });
                const data = await response.json();
                localStorage.setItem("points", data.score);
                setPoints(data.score); // Manually update the state
            } catch (error) {
                console.error("Network or server error:", error);
            }
        };

        fetchPoints();


            const handleStorageChange = (event) => {
                if (event.key === 'points') {
                    setPoints(event.newValue || 0);
                }
            };
    
            window.addEventListener('storage', handleStorageChange);
    
            // Cleanup the event listener on component unmount
            return () => {
                window.removeEventListener('storage', handleStorageChange);
            };
    }, []);

    return (
        <div
            style={{
                position: 'fixed',
                top: '10px',
                left: '10px',
                zIndex: 3,
                backgroundColor: '#00b300',
                color: '#FFF',
                padding: '5px',
                borderRadius: '5px',
                fontFamily: 'monospace',
                fontSize: '1.2rem',
            }}
        >
           {username}: {points} points
        </div>
    );
};

export default Points;
