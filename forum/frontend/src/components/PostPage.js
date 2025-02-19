import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import PostContainer from './PostContainer';
import HeaderPost from './HeaderPost';

const PostPage = ({ backendUrl }) => {
    const { postId } = useParams();
    const [postData, setPostData] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchPost = async () => {
            try {
                const token = sessionStorage.getItem('jwtToken');
                const response = await fetch(`http://${backendUrl}/posts/${postId}`, {
                    method: 'GET',
                    credentials: 'include',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                        'Instance-Id': localStorage.getItem('instanceId'),
                    }
                });
                if (response.ok) {
                    const data = await response.json();
                    console.log(data);
                    setPostData(data);
                } else {
                    console.error('Failed to fetch post:', response.status);
                }
            } catch (error) {
                console.error('Error fetching post:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchPost();
    }, [postId]);

    if (loading) {
        return <div>Loading...</div>;
    }

    if (!postData) {
        return <div>Post not found</div>;
    }

    return (
        <>
            <HeaderPost postID={postData.id} comments_nr={postData.comments.length} />
            <PostContainer postData={postData} />
        </>);
};

export default PostPage;
