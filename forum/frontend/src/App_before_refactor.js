import React, { useState, useEffect } from 'react';
import './App.css'
import Header from './components/Header.js'
import Header2 from './components/Header2.js'
import Post from './components/Post.js'
import HeaderPost from './components/HeaderPost.js';
import PostContainer from './components/PostContainer.js'
import BackendURL from './BackendURL.js';

function App() {
    const [posts, setPosts] = useState([]);
    const [postCount, setPostCount] = useState(0);
    const [activeSection, setActiveSection] = useState('');

    const backendUrl = 'localhost:8080';

    useEffect(() => {
        fetchPosts();
    }, []);

    const fetchPosts = async () => {
        try {
            const response = await fetch(`http://${backendUrl}/posts/1`, {
                method: 'GET',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json',
                }
            });
            if (response.ok) {
                const posts_res = await response.json();
                setPosts(posts_res);
                setPostCount(posts_res.length);
            } else {
                console.error('Failed to fetch posts:', response.status);
            }
        } catch (error) {
            console.error('Error fetching posts:', error);
        }
    };

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
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(postData)
            });

            if (response.ok) {
                const createdPost = await response.json();
                setPosts((prevPosts) => [...prevPosts, createdPost]);
                setPostCount((prevCount) => prevCount + 1);
            } else {
                console.error('Failed to create post. Status:', response.status);
            }
        } catch (error) {
            console.error('Error while creating post:', error);
        }
    };

    const setRoleCookie = (ROLE) => {
        const expires = new Date(Date.now() + 86400 * 1000).toUTCString();
        document.cookie = `ROLE=${ROLE}; expires=${expires}; path=/; SameSite=Strict`;
        console.log('ROLE cookie set');
        fetchPosts();
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
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(adminLoginData)
            });

            if (response.ok) {
                console.log('login success');
                setRoleCookie("ROLE_ADMIN");
            } else {
                console.error('Failed to login. Status:', response.status);
            }
        } catch (error) {
            console.error('Error while loggin in', error);
        }
    };


    const handleSectionChange = (section) => {

        setActiveSection(section);
    };

    return (
        <div className="App">
            <Header handleSectionChange={handleSectionChange} createPost={adminLoginTest} />

            {activeSection === 'malarstwo' && (
                <>
                    <Header2 postCount={postCount} />
                    <div className="posts-list">
                        {posts.map((post, index) => (
                            <Post key={index} post={post} />
                        ))}
                    </div>
                </>
            )}

            {activeSection === 'wedkarstwo' && (
                <>
                    <HeaderPost postID={1} comments_nr={2} />
                    <PostContainer postData={postData} />
                </>
            )}
        </div>
    );
}
export default App;