import React, { useState, useEffect } from 'react';
import './AccountPage.css';
import Header from "./components/BankHeader";
import Footer from "./components/BankFooter";
import TransferForm from './components/accountpage_body_components/TransferForm';
import AccountPanel from './components/accountpage_body_components/AccountPanel';
import AccountsList from './components/accountpage_body_components/AccountsList';
import { useNavigate } from 'react-router-dom';
import mapStringToName from './components/accountpage_body_components/NameFunction';
import BackendURL from './BackendURL';
import DollarBackground from './components/DollarBackground';

const AccountPage = () => {

    const [tokenPresent, setTokenPresent] = useState(false);
    const [username, setUsername] = useState('');
    const [accounts, setAccounts] = useState([]);
    const [message, setMessage] = useState('');
    const [selectedAccount, setSelectedAccount] = useState({});
    const [firstAccountCreated, setFirstAccountCreated] = useState(false);
    const [semaphore, setSemaphore] = useState(0);
    const [allTransactions, setAllTransactions] = useState([])
    const navigate = useNavigate();
    const [dollarMultiplier, setDollarMultiplier] = useState(0);
    let techCounter = 0;
    const [userFlag, setUserFlag] = useState("");



    useEffect(() => {
        const token = sessionStorage.getItem('token');
        const accountCreated = sessionStorage.getItem('firstAccountCreated');

        if (token) {
          setTokenPresent(true);
          const storedUsername = sessionStorage.getItem('username');
          setUsername(storedUsername || '');
        }else{
            navigate('/');
        }

        const fetchUserFlag = async () => {
            const backendurl = await BackendURL();
            const token = sessionStorage.getItem('token');
            try{
                const response = await fetch(`http://${backendurl}/users/flag`, {
                    method: 'GET',
                    headers: {
                      'Authorization': `Bearer ${token}`,
                      'Instance-Id': localStorage.getItem('instanceId'),
                    },
                  });
                if(response.ok){
                    const result = await response.json();
                    if (result.status !== "Nope."){
                        setUserFlag(result.status);
                    }
                }else if(response.status === 403){
                }
            }catch(error){
                console.error("Error: ",error);
            }
        };

        const request_accounts = async () =>{
            console.log("tech counter: ",techCounter);
            techCounter+=1;
            const backendurl= await BackendURL();
            try{
                const response = await fetch(`http://${backendurl}/accounts/me`, {
                    method: 'GET',
                    headers: {
                      'Authorization': `Bearer ${token}`,
                      'Instance-Id': localStorage.getItem('instanceId'),
                    },
                  });
                if(response.ok){
                    const result = await response.json();
                    if(result.length === 0){
                    }else{
                        setFirstAccountCreated(true);
                        sessionStorage.setItem('firstAccountCreated','true');
                        setAccounts(result);
                        setSelectedAccount(result[0]);
                        if(dollarMultiplier != result.length){
                            setDollarMultiplier(result.length);
                        }
                    }

                }else if(response.status === 403){
                }
            }catch(error){
                console.error("Error: ",error);
            }
        };

        const post_first_account = async () => {
            
            const balance = 1000.00;
            const data = {balance,};
            const backendurl = await BackendURL();
            try{
                const response = await fetch(`http://${backendurl}/accounts`,{
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type':'application/json',
                        'Instance-Id': sessionStorage.getItem('instanceId'),
                    },
                    body: JSON.stringify(data),
                });
                if(response.ok){
                    const result = await response.json();
                    setAccounts([result]);
                    setSelectedAccount(result);
                    setFirstAccountCreated(true);
                    sessionStorage.setItem('firstAccountCreated', 'true');
                }else if(response.status === 403){
                }
            }catch(error){
                console.error("Error: ",error);
            }
        };

        fetchUserFlag();
        request_accounts();
        
      }, [firstAccountCreated, dollarMultiplier]);
    
    const handleAccountSelection = (account) => {
        setSelectedAccount(account);
    };
    const gradientAngle = 90;
    const gradientColors = ['rgba(84,46,81,1)', 'rgba(183,187,143,1)', 'rgba(0,255,175,1)'];
    return (
        <div className="accountPageContainer">
            <Header tokenPresent={tokenPresent} username={username}/>
            <DollarBackground multiplier={dollarMultiplier} gradientAngle={gradientAngle} gradientColors={gradientColors}/>
            <div className="mainContent">
                <div className="transfers">  
                    <TransferForm accounts={accounts}/>
                </div>
                <div className="accountSelection">
                    <AccountsList
                        accounts={accounts}
                        selectedAccount={selectedAccount}
                        onSelectAccount={handleAccountSelection}
                    />
                </div>
                <div className="accountPanel">
                    {userFlag && <div className="AccountFlag" > <p> <b>User flag: {userFlag} </b></p> </div>}
                    {selectedAccount.accountNumber ? (<AccountPanel
                        number={selectedAccount.accountNumber.replace(/(.{4})(.{4})(.{4})/, '$1 $2 $3')}
                        name={mapStringToName(selectedAccount.accountNumber)}
                        balance={selectedAccount.balance}
                        s_transactions={selectedAccount.sourceTransactions}
                        d_transactions={selectedAccount.destinationTransactions}
                    />):(
                        <div className="noAccountInfo">
                            <p>You do not have an account yet!<br></br><b>Create it for free on the middle panel!</b></p>
                        </div>
                    )}
                </div>
            </div>
            <Footer />
        </div>
    );
};

export default AccountPage;