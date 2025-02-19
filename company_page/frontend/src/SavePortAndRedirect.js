import React, { useEffect } from 'react';
import { Navigate, useParams } from 'react-router-dom';


const SavePortAndRedirect = () => {
  const { data } = useParams();

  const decodedData = JSON.parse(atob(data));
  const instanceId = decodedData.instance_id;
  if (!instanceId){
    instanceId = 'instance-id'
  }

  useEffect(() => {
    localStorage.setItem('instanceId', instanceId);
  }, [instanceId]);
  return <Navigate to="/" />;
};

export default SavePortAndRedirect;