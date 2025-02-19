import React, { useState, useEffect } from 'react';
import './TransferForm.css';
import mapStringToName from './NameFunction';
import BackendURL from '../../BackendURL';
const TransferForm = ({ accounts }) => {

    const [message, setMessage] = useState('');
    const [token, setToken] = useState('');
    const [tokenPresent, setTokenPresent] = useState(false);
    const [username, setUsername] = useState('');
    const [sourceAccount, setSourceAccount] = useState('');
    const [destinationAccount, setDestinationAccount] = useState('');
    const [amount, setAmount] = useState(0.0);
    const [comment, setComment] = useState('');

    const [toWarning, setToWarning] = useState(false);
    const [amountWarning, setAmountWarning] = useState(false);
    const [formWarning, setFormWarning] = useState(false);
    const [flag, setFlag] = useState('');
    const maxLength = 100;

    const checkMaxBalance = async () =>{
        const backendurl = await BackendURL();
        const token = sessionStorage.getItem('token');
        try{
            const response = await fetch(`http://${backendurl}/accounts/checkBalance`,{
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Instance-Id': localStorage.getItem('instanceId'),
                },
            });
            if(response.ok){
                const result = await response.json();
                if (result.status.startsWith("TGHM"))
                {
                    setFlag(result.status);
                }
            }
        }catch(error){
            console.error("Error: ",error);
        }
    }
    
    useEffect(() => {
        const _token = sessionStorage.getItem('token');
        if (_token) {
            setTokenPresent(true);
            const storedUsername = sessionStorage.getItem('username');
            setUsername(storedUsername || '');
            setToken(_token);
        }
        checkMaxBalance();
        
        if(flag !== ''){
            alert(flag);
        }
    });

    

    useEffect(() => {
        const _token = sessionStorage.getItem('token');
        if (_token) {
            setTokenPresent(true);
            const storedUsername = sessionStorage.getItem('username');
            setUsername(storedUsername || '');
            setToken(_token);
        }
        checkMaxBalance();
        if(flag !== ''){
            alert(flag);
        }
    });


    const request_make_transaction = async (sourceAccountNumber, destinationAccountNumber, amount, title) => {
        const backendurl = await BackendURL();
        const data = { sourceAccountNumber, destinationAccountNumber, amount, title};
        try {
            const response = await fetch(`http://${backendurl}/transactions`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                    'Instance-Id': localStorage.getItem('instanceId'),
                },
                body: JSON.stringify(data),
            });
            if (response.ok) {
                const result = await response.json();
                setAmountWarning(false);
                setToWarning(false);
                setAmountWarning(false);
                setFormWarning(false);
                setSourceAccount('');
                setDestinationAccount('');
                setAmount(0.0);
                setComment('');
                setMessage('');
                await checkMaxBalance();

                window.location.reload();


            }else if(response.status === 400){
                setMessage("Niepoprawna wartość transakcji");
                setAmountWarning(true);
            }
        } catch (error) {
            setMessage('Nie udalo sie wykonac transakcji');
            console.error('Error: ', error);
        }
    }

    const format_string_to_account_form = (str) => {
        return str.replace(/\s+/g, '');
    }
    const validate_account_number = (str) => {
        return /^\d{12}$/.test(str);
    }
    const validate_amount = (flt) => {
        return flt > 0.0;
    }
    const handleSubmit = (e) => {
        e.preventDefault();

        const formattedSourceAccount = format_string_to_account_form(sourceAccount);
        const formattedDestinationAccount = format_string_to_account_form(destinationAccount);

        if (formattedSourceAccount === formattedDestinationAccount) {
            setMessage("Accounts cannot be the same!");
            setToWarning(true);
            setFormWarning(true);
            return;
        }else{
            setToWarning(false);
        }

        if (!validate_account_number(formattedDestinationAccount)) {
            setMessage("Invalid account number!");
            setToWarning(true);
            setFormWarning(true);
            return;
        }else{
            setToWarning(false);
        }

        const amountFloat = parseFloat(amount);
        if (!validate_amount(amountFloat)) {
            setMessage("Invalid transaction amount!");
            setAmountWarning(true);
            setFormWarning(true);
            return;
        }

        request_make_transaction(formattedSourceAccount, formattedDestinationAccount, amountFloat, comment);
    }
    const onSelectAccountChange = (e) => {
        setSourceAccount(e.target.value);
    }
    const onDestinationAccountChange = (e) => {
        setDestinationAccount(e.target.value);
    }
    const onAmountChange = (e) => {
        setAmount(e.target.value);
    }
    const onCommentChange = (e) => {
        setComment(e.target.value);
    }
    return (
        <div className="transferForm">
            <div className="titleContainer">
                <h2>New transaction</h2>
            </div>
            <form onSubmit={handleSubmit}>
                <div className="formFields">
                    <label>Choose account</label>
                    <select
                        className="inputField selectField"
                        value={sourceAccount}
                        onChange={onSelectAccountChange}
                        required>
                        <option value="">-- Select source account --</option>
                        {accounts.map((account, index) => (
                            <option key={index} value={account.accountNumber}>
                                {account.accountNumber.replace(/(.{4})(.{4})(.{4})/, '$1 $2 $3')} - Konto {mapStringToName(account.accountNumber)}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="formFields">
                    <label>Destination account number:</label>
                    <input
                        type="text"
                        className="inputField"
                        value={destinationAccount}
                        onChange={onDestinationAccountChange}
                        maxLength={50}
                        required />
                    {toWarning &&
                        <div className="warning">
                            Account number is incorrect.
                        </div>
                    }
                </div>


                <div className="formFields">
                    <label>Transaction amount:</label>
                    <input
                        type="number"
                        className="inputField"
                        value={amount}
                        onChange={onAmountChange}
                        required />
                    {amountWarning &&
                        <div className="warning">
                            Given amount is incorrect.
                        </div>
                    }
                </div>


                <div className="formFields comment">
                    <label className="lab">Title:</label>
                    <input
                        type="text"
                        className="inputField"
                        value={comment}
                        onChange={onCommentChange} 
                        maxLength={maxLength} 
                        />
                    <p className="charactersLeft">{maxLength - comment.length} left</p>
                    
                </div>
                {formWarning &&
                        <div className="warning">
                            Form has some errors. The transfer hasn't been finalized.
                        </div>
                }
                <div className="submitButtonContainer">
                    <button type="submit" className="submitButton">
                        Transfer!
                    </button>
                </div>
                
            </form>
        </div>
    );
};

export default TransferForm;

