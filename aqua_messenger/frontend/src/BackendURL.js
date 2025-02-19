
const BackendURL = () => {
    if (process.env.NODE_ENV === 'development') {
        return 'localhost:8080';
    }

    return `${localStorage.getItem(`instanceId`)}.ctfgame.pl`;

};

export default BackendURL;