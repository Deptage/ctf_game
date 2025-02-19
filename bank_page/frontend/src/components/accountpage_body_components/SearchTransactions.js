import BackendURL from "../../BackendURL";

const searchTransactions = async (title) => {
    const backendurl = await BackendURL();
    const token = sessionStorage.getItem('token');
    try{
        const response = await fetch(`http://${backendurl}/transactions/searchByTitle?title=${title}`,{
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Instance-Id': localStorage.getItem('instanceId'),
            },
        });
        if(response.ok){
            const result = await response.json();
            return result;
        }
    }catch(error){
        console.error("error: ",error);
    }
}

export default searchTransactions;