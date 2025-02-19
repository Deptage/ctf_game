import React, {useState, useEffect} from 'react';
import './AccountsList.css';
import mapStringToName from './NameFunction';
import BackendURL from '../../BackendURL';
const AccountsList = ({ accounts, selectedAccount, onSelectAccount }) => {
    const post_account = async () => {
        const balance = 1000.00;
        const data = {balance,};
        const backendurl = await BackendURL();
        const token = sessionStorage.getItem('token');
        try{
            const response = await fetch(`http://${backendurl}/accounts`,{
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type':'application/json',
                    'Instance-Id': localStorage.getItem('instanceId'),
                },
                body: JSON.stringify(data),
            });
            if(response.ok){
                const result = await response.json();
            }
        }catch(error){
            console.error("Error: ",error);
        }
    };
    const handleButtonClick = () => {
        post_account();
        window.location.reload();
    }

    const interpolateColor = (balance) => {
        const minBalance = 0;
        const maxBalance = 500;
        const clampedBalance = Math.min(Math.max(balance, minBalance), maxBalance);
        const ratio = clampedBalance / maxBalance;
        const red = Math.round(255 * (1 - ratio));
        const green = Math.round(255 * ratio);
        
        return `rgb(${red}, ${green}, 0)`;
      };

    return (
        <div className="accountContainer">
            <div className="accountsList">
                {accounts.map((account, index) => (
                    <div
                        key={index}
                        className={`accountItem ${
                            selectedAccount.accountNumber === account.accountNumber ? 'accountItemSelected' : ''
                        }`}
                        onClick={() => onSelectAccount(account)}
                    >
                        <div className="leftInfo">
                            <h3>Account {mapStringToName(account.accountNumber)}</h3>
                            <p>{account.accountNumber.replace(/(.{4})(.{4})(.{4})/, '$1 $2 $3')}</p>
                        </div>
                        <div className="rightInfo"
                            style={{ borderColor: interpolateColor(account.balance) }}>
                            <p>${account.balance}</p>
                        </div>
                        
                    </div>
                ))}
            </div>
            {accounts.length < 3 &&
            <div className="buttonContainer">
                <button className="accountButton" onClick={handleButtonClick}>Add accounts</button>
            </div>}
        </div>
    );
};

export default AccountsList;