import React, { useState, useEffect } from 'react';

const Clock = () => {
  const [time, setTime] = useState(new Date());

  useEffect(() => {
    const timerId = setInterval(() => {
      setTime(new Date());
    }, 1000);

    return () => clearInterval(timerId);
  }, []);

  const formatTime = (date) => {
    return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit'});
  };

  return (
    <div style={styles.clock}>
      {formatTime(time)}
    </div>
  );
};

const styles = {
  clock: {
    position: 'fixed',
    bottom: '10px',
    right: '10px',
    backgroundColor: '#333',
    color: '#FFF',
    padding: '10px',
    borderRadius: '5px',
    fontFamily: 'monospace',
    fontSize: '1.2rem',
    zIndex: 3,
  }
};

export default Clock;
