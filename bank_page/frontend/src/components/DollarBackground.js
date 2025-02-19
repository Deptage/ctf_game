import React from 'react';
import './DollarBackground.css';

const DollarBackground = ({ multiplier, gradientAngle, gradientColors }) => {
    const numberOfDollarSigns = parseInt(multiplier)*20
    const dollarSigns = Array.from({ length: numberOfDollarSigns }, (_, index) => {
        const top = Math.random() * 100;
        const left = Math.random() * 100;
        return (
            <div
                key={index}
                className="dollar-sign"
                style={{ top: `${top}%`, left: `${left}%`, animationDelay: `${Math.random() * 4}s` }}
            >
                $
            </div>
        );
    });
    const gradientString = `linear-gradient(${gradientAngle}deg, ${gradientColors.join(', ')})`;

    return (
        <div className="dollar-background" style={{ background: gradientString }}>
            {dollarSigns}
        </div>
    );
};

export default DollarBackground;