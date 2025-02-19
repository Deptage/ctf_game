import React, { useState, useEffect } from 'react';
import './App.css'
import Header from './components/Header.js'
import Header2 from './components/Header2.js'
import Post from './components/Post.js'
import HeaderPost from './components/HeaderPost.js';
import PostContainer from './components/PostContainer.js'

function App() {
    const [posts, setPosts] = useState([]);
    const [postCount, setPostCount] = useState(0);
    const [activeSection, setActiveSection] = useState('');

    useEffect(() => {
        fetchPosts();
    }, []);

    const fetchPosts = () => {
        const postDataTemplate = [
            {
                title: "Post Title",
                content: "Post content...",
                comments: [],
                author: {
                    username: "john_doe",
                    role: "USER"
                }
            },
            {
                title: "Another Post Title",
                content: "More post content...",
                comments: [
                    {
                        content: "Great post!",
                        author: {
                            username: "jane_doe",
                            role: "USER"
                        }
                    },
                    {
                        content: "Great post!",
                        author: {
                            username: "jane_doe",
                            role: "USER"
                        }
                    },
                    {
                        content: "Great post!",
                        author: {
                            username: "jane_doe",
                            role: "USER"
                        }
                    },
                    {
                        content: "Great post!",
                        author: {
                            username: "jane_doe",
                            role: "USER"
                        }
                    },
                    {
                        content: "Great post!",
                        author: {
                            username: "jane_doe",
                            role: "USER"
                        }
                    },
                    {
                        content: "Great post!",
                        author: {
                            username: "jane_doe",
                            role: "USER"
                        }
                    }
                ],
                author: {
                    username: "admin",
                    role: "ADMIN"
                }
            }
        ];

        setPosts(postDataTemplate);
        setPostCount(postDataTemplate.length);
    };

    const createPost = () => {
        const newPost = {
            title: "New Post",
            content: "This is the content of the new post.",
            comments: [],
            author: {
                username: "john_doe",
                role: "USER"
            }
        };

        setPosts((prevPosts) => [...prevPosts, newPost]);
        setPostCount((prevCount) => prevCount + 1);

        console.log('Post created successfully:', newPost);
    };

    const test_post = {
        id: 1,
        title: "Another Post Title",
        content: "More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...More post content...",
        comments: [
            {
                content: "Great post!",
                author: {
                    username: "jane_doe",
                    role: "USER"
                }
            },
            {
                content: "Great post!",
                author: {
                    username: "jane_doe",
                    role: "USER"
                }
            },
            {
                content: "Great post!",
                author: {
                    username: "jane_doe",
                    role: "USER"
                }
            },
            {
                content: "Great post!",
                author: {
                    username: "jane_doe",
                    role: "USER"
                }
            },
            {
                content: "Great post!",
                author: {
                    username: "jane_doe",
                    role: "USER"
                }
            },
            {
                content: "Great post!",
                author: {
                    username: "jane_doe",
                    role: "USER"
                }
            },
            {
                content: "Great post!",
                author: {
                    username: "jane_doe",
                    role: "USER"
                }
            },
            {
                content: "Great post!",
                author: {
                    username: "jane_doe",
                    role: "USER"
                }
            },
            
        ],
        author: {
            username: "admin",
            role: "ADMIN"
        }
    }

    const handleSectionChange = (section) => {
        setActiveSection(section);
    };


    return (
        <div className="App">
            <Header handleSectionChange={handleSectionChange} createPost={createPost} />
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
                    <PostContainer postData={test_post} />
                </>
            )}
        </div>
    );
}
export default App;