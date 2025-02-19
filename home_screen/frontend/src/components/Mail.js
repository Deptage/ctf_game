import React, { useState, useEffect, useImperativeHandle, forwardRef } from 'react';
import '../styles/Mail.css'

const MailList = forwardRef((props, ref) => {

    const [initialMails, setInitialMails] = useState([]);
    const [filters, setFilters] = useState([true, false, false]);
    const [displayMails, setDisplayMails] = useState(initialMails)
    const [unfilteredCounter, setUnfilteredCounter] = useState(0);
    const [readCounter, setReadCounter] = useState(0);
    const [unreadCounter, setUnreadCounter] = useState(0);
    const request_mails = async () => {
        const token = localStorage.getItem('token');
        try{
            const response = await fetch(`http://localhost:8080/mail`,{
                method:'GET',
                headers:{
                    'Authorization': `Bearer ${token}`,
                }
            });
            if(response.ok){
                const result = await response.json();
                setInitialMails(result.reverse());
            }
        }catch(error){
            console.error("Error: ",error);
        }
    }

    const request_read_mail = async (id) => {
        const token = localStorage.getItem('token');
        try{
            const response = await fetch(`http://localhost:8080/mail/${id}/read`,{
                method:'PUT',
                headers:{
                    'Authorization': `Bearer ${token}`,
                }
            });
            if(response.ok){
                const result = await response.json();
            }
        }catch(error){
            console.error("Error: ",error);
        }
    }

    const onUnfiltered = () => {
        setFilters([true, false, false]);
    }

    const onRead = () => {
        setFilters([false, true, false]);
    }

    const onUnread = () => {
        setFilters([false, false, true]);
    }

    const handleRead = async (id) => {
        request_read_mail(id);
        request_mails();
    }
    const countMailTags = (mails) => {
        let uf = 0;
        let r = 0;
        let ur = 0;
        for(let mail of mails){
            uf=uf+1;
            if(mail.read){
                r=r+1;
            }else{
                ur=ur+1;
            }
        }
        setUnfilteredCounter(uf);
        setReadCounter(r);
        setUnreadCounter(ur);
    }

    useEffect(() => {
        request_mails();
    },[])
    useImperativeHandle(ref, () => ({
        request_mails
    }));
    useEffect(() => {
        if (filters[0]) {
            setDisplayMails(initialMails);
        } else if (filters[1]) {
            setDisplayMails(initialMails.filter(mail => mail.read === true));
        } else if (filters[2]) {
            setDisplayMails(initialMails.filter(mail => mail.read === false));
        }
    }, [initialMails, filters]);

    useEffect(()=>{
        countMailTags(initialMails);
    },[initialMails]);
    return (
        <>
            <div className="mail-filter">
                <p onClick={onUnfiltered} className={`${filters[0] ? 'pressed' : ''}`}  >Unfiltered <b>{unfilteredCounter}</b></p>
                <p onClick={onRead} className={`${filters[1] ? 'pressed' : ''}`}>Read <b>{readCounter}</b></p>
                <p onClick={onUnread} className={`${filters[2] ? 'pressed' : '' } ${unreadCounter > 0 ? 'mail-notification' : '' }`}>Unread <b>{unreadCounter}</b></p>
            </div>
            <div className="mail-list">
                {displayMails.length > 0 ? (
                    displayMails.map((email) => (
                        <div
                            key={email.id}
                            onClick={() => handleRead(email.id)}
                            className={`mail-item ${email.read ? 'read' : ''}`}
                        >
                            <div className="mail-sender">from: {email.sender}</div>
                            <div className="mail-subject">SUBJECT: {email.topic}</div>
                            <div className="mail-snippet">{email.content}</div>
                        </div>
                    ))
                ) : (
                    <div className="mail-no-mails"><b>No mails to display!</b></div>
                )}
            </div>
        </>

    );
});

export default MailList;

