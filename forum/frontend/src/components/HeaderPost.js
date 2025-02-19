import React from "react";
import '../styles/HeaderPost.css'

const HeaderPost = ({postID, comments_nr}) => {
    return (
        <div className="header2">
            <span className="napis-po-lewej">Post #{postID}</span>
            <span className="napis-po-prawej">Comments: {comments_nr}</span>
        </div>
    );
}

export default HeaderPost;