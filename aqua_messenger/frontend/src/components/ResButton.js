import React from 'react';
import '../styles/ResButton.css'

function ResButton({ message, onClick }) {
  return (
    <div className='resbutton' onClick={onClick}>
      {message}
    </div>
  );
}

export default ResButton;