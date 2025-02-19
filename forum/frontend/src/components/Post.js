import React from "react";
import '../styles/Post.css';
import { useNavigate } from "react-router-dom";

function stringToColor(str) {
    let hash = 0;
    for (let i = 0; i < str.length; i++) {
        hash = str.charCodeAt(i) + ((hash << 5) - hash);
    }
    let color = '#';
    for (let i = 0; i < 3; i++) {
        const value = (hash >> (i * 8)) & 0xFF;
        color += ('00' + value.toString(16)).slice(-2);
    }
    return color;
}


const Post = ({post}) => {
    const avatarColor = stringToColor(post.author.username);
    const commentCount = post.comments ? post.comments.length : 0;
    const navigate = useNavigate();

    const handleHover = () => {
        navigate(`/posts/${post.id}`);
    };

    return (
        <div className="post-container-1" onClick={handleHover}>
            <div className="avatar-1">
                <div className="avatar-circle-1" style={{ backgroundColor: avatarColor }}>{post.author.username[0].toUpperCase()}</div>
            </div>
            <div className="post-content-1">
                <div className="post-username-1">{post.author.username}</div>
                <div className="post-title-1">{post.title}</div>
            </div>
            <div className="post-comments-1">
                <span>{post.comments.length} comments</span>
            </div>
        </div>
    );
}

export default Post;