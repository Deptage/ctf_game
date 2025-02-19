import React, {useState,useEffect} from 'react';
import './AccountPanel.css';
import TransactionList from './TransactionList';
import { FaSearch } from 'react-icons/fa';
import searchTransactions from './SearchTransactions';
const AccountPanel = ({ number, name, balance, s_transactions, d_transactions }) => {
    const formattedBalance = (typeof balance === 'number' && !isNaN(balance)) ? balance.toFixed(2) : '0.00'; 
    const [title,setTitle] = useState('');
    const [sourceTransactions, setSourceTransactions] = useState(s_transactions);
    const [destinationTransactions, setDestinationTransactions] = useState(d_transactions);
    const maxLength = 100;
    let result = [];

    useEffect(() => {
        setSourceTransactions(s_transactions);
        setDestinationTransactions(d_transactions);
    }, [s_transactions, d_transactions]);
    
    const onTitleChange = (event) => {
        setTitle(event.target.value);
    };
    const handleSubmit = async (event) =>{
        event.preventDefault();
        if(title === ''){
            setSourceTransactions(s_transactions);
            setDestinationTransactions(d_transactions);
        }else{
            event.preventDefault();
            try{
                result = await searchTransactions(title);
                const {source, destination} = disambiguateTransactions(result);
                setSourceTransactions(source);
                setDestinationTransactions(destination);
            } catch (error){
                console.error('Error fetching transactions');
            }
            setTitle('');
        }
        
    }
    const disambiguateTransactions = (transactions) => {
        let source = [];
        let destination = [];
        let thisAccount = number;

        transactions.forEach((transaction) =>{
            const formattedSourceAccountNumber = transaction.sourceAccountNumber;
            const formattedThisAccount = thisAccount.replace(/\s+/g, '');

            if(formattedSourceAccountNumber === formattedThisAccount){
                source.push(transaction);
            }
            else{
                destination.push(transaction);
            }
        });
        return {source, destination};
    }
    return (
        <div className="accountInfo">
            <h2>Account {name}</h2>
            <p className="balance">${formattedBalance}</p>
            <p>Account number: {number}</p>
            <form className="search" onSubmit={handleSubmit}>
                <input
                    type="text"
                    className="searchBox"
                    placeholder="Search transactions..."
                    value={title}
                    onChange={onTitleChange}
                    maxLength={maxLength} 
                />
                <p className="charactersLeft">{maxLength - title.length} left</p>
            </form>
            <TransactionList sourceTransactions={sourceTransactions} destinationTransactions={destinationTransactions} />
        </div>
    );
};

export default AccountPanel;