import React, { useRef, useState, useEffect } from 'react';
import Draggable from 'react-draggable';
import '../styles/DraggableWindow.css';

const DraggableWindow = ({ defaultPositionPercent, children, style }) => {
  const windowRef = useRef(null);
  const [bounds, setBounds] = useState(null);

  const calculateDefaultPosition = () => {
    const x = (window.innerWidth * defaultPositionPercent.x) / 100;
    const y = (window.innerHeight * defaultPositionPercent.y) / 100;
    return { x, y };
  };

  const calculateBounds = () => {
    if (windowRef.current) {
      const { offsetWidth, offsetHeight } = windowRef.current;
      setBounds({
        left: 0,
        top: 0,
        right: window.innerWidth - offsetWidth,
        bottom: window.innerHeight - offsetHeight,
      });
    }
  };

  useEffect(() => {
    calculateBounds();
    window.addEventListener('resize', calculateBounds);

    return () => {
      window.removeEventListener('resize', calculateBounds);
    };
  }, []);

  return (
    <Draggable defaultPosition={calculateDefaultPosition()} bounds={bounds}>
      <div ref={windowRef} className="draggable-window" style={style}>
        {children}
      </div>
    </Draggable>
  );
};

export default DraggableWindow;