import React from 'react';

const Transaction = ({ amount, accountNumber, name }) => {
    return (
        <li style={{ color: 'green', marginBottom: '10px' }}>
            + ${amount.toFixed(2)} <span style={{ color: '#ccc' }}>{name} (Acct: {accountNumber})</span>
        </li>
    );
};

export default Transaction;