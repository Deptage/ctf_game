import React, { useEffect, useRef, useState } from 'react';
import '../styles/MatrixBackground.css';

const MatrixBackground = ({ paused }) => {
  const canvasRef = useRef(null);
  const [intervalId, setIntervalId] = useState(null);

  useEffect(() => {
    const canvas = canvasRef.current;
    const ctx = canvas.getContext('2d');
    
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;

    const fontSize = 20;
    const columns = canvas.width / fontSize;
    
    const drops = Array.from({ length: columns }).map(() => Math.random() * canvas.height);

    const draw = () => {
      ctx.fillStyle = 'rgba(0, 0, 0, 0.05)';
      ctx.fillRect(0, 0, canvas.width, canvas.height);

      ctx.fillStyle = '#0F0';
      ctx.font = `${fontSize}px monospace`;

      drops.forEach((y, i) => {
        const text = String.fromCharCode(33 + Math.random() * 93);
        const x = i * fontSize;
        ctx.fillText(text, x, y * fontSize);

        if (y * fontSize > canvas.height && Math.random() > 0.975) {
          drops[i] = 0;
        }
        drops[i]++;
      });
    };

    
    const interval = paused ?  50:999999999;

    const newIntervalId = setInterval(draw, interval);
    setIntervalId(newIntervalId);

    return () => clearInterval(newIntervalId);
  }, [paused]);

  return <canvas ref={canvasRef} className="matrix-canvas"></canvas>;
};

export default MatrixBackground;
