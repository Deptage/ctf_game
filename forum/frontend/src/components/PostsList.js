import React, { useState, useEffect } from 'react';
import Post from './Post';
import '../styles/PostsList.css'

const PostsList = ({ backendUrl }) => {
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [postCount, setPostCount] = useState(0);

    const fetchPosts = async () => {
        const token = sessionStorage.getItem('jwtToken');
        if (token) {
            try {
                const response = await fetch(`http://${backendUrl}/posts`, {
                    method: 'GET',
                    credentials: 'include',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                        'Instance-Id': localStorage.getItem('instanceId'),
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
            } finally {
                setLoading(false);
            }
        } else {
            console.error('Token not found');
            setLoading(false);
        }
    };

    const getFlag = async () => {
        const token = sessionStorage.getItem('jwtToken');
        if (token) {
            try {
                const response = await fetch(`http://${backendUrl}/flag`,
                    {
                        method: 'GET',
                        credentials: 'include',
                        headers: {
                            'Authorization': `Bearer ${token}`,
                            'Content-Type': 'application/json',
                            'Instance-Id': localStorage.getItem('instanceId'),
                        }
                    }
                );
                if (response.ok) {
                    const flag = await response.json();
                    return flag.flag;
                } else {
                    return false;
                }
            } catch (error) {
                return false;
            }
        } else {
            return false;
        }
    };

    useEffect(() => {
        const initialize = async () => {
            await fetchPosts();
            const isFlag = localStorage.getItem('isFlag');
            if (!isFlag) {
                const flag = await getFlag();
                if (flag) {
                    alert(flag);
                    localStorage.setItem('isFlag', 'true');
                }
            }
        };
        
        initialize();
    }, []);
    

    if (loading) {
        return <div>Loading...</div>;
    }

    return (
        <>
            <div className="header2">
                <span className="napis-po-lewej">All posts</span>
                <span className="napis-po-prawej">Found posts: {postCount}</span>
            </div>
            <div className="posts-list">
                {posts.length > 0 ? (
                    posts.map((post, index) => <Post key={index} post={post} />)
                ) : (
                    <div className='no-posts-avail'>No posts available</div>
                )}
            </div>
        </>
    );
};

export default PostsList;
