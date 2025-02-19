import React, { useState, useEffect } from 'react';
import '../styles/Banner.css'


const Banner = ({ link, InstanceRun, Instance, stopInstance, timeLeftInstance, extendTime }) => {

    //const [timeLeft, setTimeLeft] = useState(3600);
    const [timeLeft, setTimeLeft] = useState(timeLeftInstance);

    useEffect(() => {
        setTimeLeft(timeLeftInstance);
    }, [timeLeftInstance]);

    useEffect(() => {
        if (timeLeft > 0) {
            const interval = setInterval(() => {
                setTimeLeft(prevTimeLeft => prevTimeLeft - 1);
            }, 1000);
            return () => clearInterval(interval);
        } else if (timeLeft === 0) {
            stopInstance();
        }
    }, [timeLeft, stopInstance]);


    const mins = Math.floor(timeLeft / 60);
    const secs = timeLeft % 60;

    const formatTime = (num) => {
        if (num < 10) {
            return `0${num}`;
        } else {
            return num;
        }
    }

    return (
        <div className="banner">
            <p className='banner-link'><a href={link} target="_blank" rel="noopener noreferrer">Proceed to the instance</a></p>
            <p className='banner-time-left'>Time left: {mins}:{formatTime(secs)}</p>
            <div>
                <button className='banner-button-reset' onClick={extendTime}>
                    Reset timer to 1 hour
                </button>
                <button className='banner-button-stop' onClick={stopInstance}>
                    Stop Instance
                </button>
            </div>
        </div>
    );
};

export default Banner;