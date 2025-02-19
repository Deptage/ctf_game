import React, { useState, useEffect } from 'react';
import './App.css'
import Header from './components/Header.js'
import Post from './components/Post.js'
import HeaderPost from './components/HeaderPost.js';
import PostContainer from './components/PostContainer.js'
import BackendURL from './BackendURL.js';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import PostsList from './components/PostsList.js';
import Login from './components/Login.js';
import { Navigate, useParams } from 'react-router-dom';
import PostPage from './components/PostPage.js'


function App() {
    const [posts, setPosts] = useState([]);
    const [backendUrl, setBackendUrl] = useState(BackendURL());

    const postData = {
        title: "Post Title",
        content: "Post content...",
        comments: [],
        author: {
            username: "john_doe",
            role: "USER"
        }
    };

    const createPost = async () => {
        try {
            const response = await fetch(`http://${backendUrl}/posts`, {
                method: 'POST',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json',
                    'Instance-Id': localStorage.getItem('instanceId'),
                },
                body: JSON.stringify(postData)
            });

            if (response.ok) {
                const createdPost = await response.json();
            } else {
                console.error('Failed to create post. Status:', response.status);
            }
        } catch (error) {
            console.error('Error while creating post:', error);
        }
    };

    const setRoleCookie = (ROLE) => {

        const expires = new Date(Date.now() + 86400 * 1000).toUTCString(); 
        document.cookie = `role=${ROLE}; expires=${expires}; path=/; domain=.ctfgame.pl; SameSite=Lax`;
        console.log('ROLE cookie set');

    };

    const adminLoginData = {
        username: "admin",
        password: "admin"
    };

    const adminLoginTest = async () => {
        try {

            const response = await fetch(`http://${backendUrl}/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Instance-Id': localStorage.getItem('instanceId'),
                },
                body: JSON.stringify(adminLoginData)
            });

            if (response.ok) {
                setRoleCookie("ROLE_ADMIN");
            } else {
                console.error('Failed to login. Status:', response.status);
            }
        } catch (error) {
            console.error('Error while loggin in', error);
        }
    };

    const SavePortAndRedirect = () => {
        const { data } = useParams();

        const decodedData = JSON.parse(atob(data));
        const port = decodedData.port;
        const instanceId = decodedData.instance_id || 'instance-id';
    
    
        
        localStorage.setItem('instanceId', instanceId);
        setBackendUrl(BackendURL());
    
        return <Navigate to="/login" />;
      };


    return (
        <Router>
            <div className="App">
                <Header createPost={adminLoginTest} />
                <Routes>
                    <Route
                        path="/posts"
                        element={
                            <>
                                <PostsList backendUrl={backendUrl} />
                            </>
                        }
                    />
                    <Route
                        path="login"
                        element={
                            <Login backendUrl={backendUrl}/>
                        }
                    />
                    <Route path="/posts/:postId" element={
                        <PostPage backendUrl={backendUrl}/>
                    }/>

                <Route path="/start/:data" element={<SavePortAndRedirect />} />
                </Routes>
            </div>
        </Router>
    );
}
export default App;