import React from "react";
import '../styles/PostContainer.css'

const PostContainer = ({ postData }) => {
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

    return (
        <div className="post-whole-container">
            <div className="sidebar">
                <div className="avatar" style={{ backgroundColor: stringToColor(postData.author.username) }}>
                    {postData.author.username[0].toUpperCase()}
                </div>
                <div className="user-data-container">
                    <p>Number of posts: {postData.author.postsCount}</p>
                    <p>Number of comments: {postData.author.commentsCount}</p>
                </div>
            </div>
            <div className="post-container">
                <div className="post">
                    <div className="post-header">
                        <div className="post-op-user">{postData.author.username}</div>
                        <div className="post-op-title">{postData.title}</div>
                    </div>
                    <div className="post-op-content">
                        {postData.content}
                    </div>
                </div>
                <div className="comments-container">
                    <div className="comments-title">Comments</div>
                    <div className="comments">
                        {postData.comments && postData.comments.length > 0 ? (
                            postData.comments.map((comment, index) => (
                                <div className="comment" key={index}>
                                    <div className="commenter-data">
                                        <div className="commenter-avatar"
                                            style={{ backgroundColor: stringToColor(comment.author.username) }}>
                                            {comment.author.username[0].toUpperCase()}
                                        </div>
                                        <div className="commenter-username">{comment.author.username}</div>
                                    </div>
                                    <div className="comment-text">{comment.content}</div>
                                </div>
                            ))
                        ) : (
                            <p>No comments available.</p>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default PostContainer;
