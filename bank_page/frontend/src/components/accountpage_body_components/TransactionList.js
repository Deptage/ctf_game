import React from 'react';
import './TransactionList.css';

const TransactionList = ({ sourceTransactions = [], destinationTransactions = []}) => {
    
    const allTransactions = [
        ...sourceTransactions.map(transaction => ({ ...transaction, type: 's' })),
        ...destinationTransactions.map(transaction => ({ ...transaction, type: 'd' }))
    ];

    allTransactions.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));

    return (
        <div>
            <h3>Previous transactions:</h3>
            <ul className="transactionList">
                {allTransactions.map((transaction, index) => (
                    <li key={index} className={transaction.type ==='d' ? 'transactionItemPositive' : 'transactionItemNegative' }>
                        <span className={transaction.type ==='d' ? 'positiveAmount' : 'negativeAmount' }>
                        {transaction.type === 'd' ? '+' : '-'}${Math.abs(Number(typeof transaction.amount === 'string' ? transaction.amount.substring(1) : transaction.amount)).toFixed(2)} {transaction.title}
                        </span>
                        <span className="transactionDate">
                           on {new Date(transaction.createdAt).toLocaleString()}
                        </span>
                        <span className="transactionAccount">
                            {transaction.type === 'd' ? `${transaction.sourceAccountNumber.replace(/(.{4})(.{4})(.{4})/, '$1 $2 $3')}` : `To ${transaction.destinationAccountNumber.replace(/(.{4})(.{4})(.{4})/, '$1 $2 $3')}`}
                        </span>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default TransactionList; 