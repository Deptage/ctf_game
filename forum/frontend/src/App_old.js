import React, { useState } from 'react';

function Forum() {
  const [posts, setPosts] = useState([]);
  const [newPostTitle, setNewPostTitle] = useState('');
  const [newPostContent, setNewPostContent] = useState('');

  const handlePostSubmit = () => {
    if (newPostTitle.trim() && newPostContent.trim()) {
      const newPost = {
        id: Date.now(),
        title: newPostTitle,
        content: newPostContent,
        comments: [],
      };
      setPosts([newPost, ...posts]);
      setNewPostTitle('');
      setNewPostContent('');
    }
  };

  const handleCommentSubmit = (postId, commentContent) => {
    if (commentContent.trim()) {
      setPosts(posts.map(post =>
        post.id === postId
          ? { ...post, comments: [...post.comments, { id: Date.now(), content: commentContent }] }
          : post
      ));
    }
  };

  return (
    <div style={styles.container}>
      <h1 style={styles.title}>Forum</h1>
      <div style={styles.newPost}>
        <input
          style={styles.input}
          type="text"
          placeholder="Post Title"
          value={newPostTitle}
          onChange={(e) => setNewPostTitle(e.target.value)}
        />
        <textarea
          style={styles.textarea}
          placeholder="Post Content"
          value={newPostContent}
          onChange={(e) => setNewPostContent(e.target.value)}
        />
        <button style={styles.button} onClick={handlePostSubmit}>Create Post</button>
      </div>
      <div style={styles.posts}>
        {posts.length > 0 ? posts.map(post => (
          <Post key={post.id} post={post} onCommentSubmit={handleCommentSubmit} />
        )) : <p style={styles.noPosts}>No posts yet.</p>}
      </div>
    </div>
  );
}

function Post({ post, onCommentSubmit }) {
  const [newComment, setNewComment] = useState('');

  const handleCommentSubmit = () => {
    onCommentSubmit(post.id, newComment);
    setNewComment('');
  };

  return (
    <div style={styles.post}>
      <h2 style={styles.postTitle}>{post.title}</h2>
      <p style={styles.postContent}>{post.content}</p>
      <div style={styles.comments}>
        <h3 style={styles.commentsTitle}>Comments</h3>
        {post.comments.map(comment => (
          <p key={comment.id} style={styles.comment}>{comment.content}</p>
        ))}
        <div style={styles.newComment}>
          <input
            style={styles.input}
            type="text"
            placeholder="Add a comment"
            value={newComment}
            onChange={(e) => setNewComment(e.target.value)}
          />
          <button style={styles.button} onClick={handleCommentSubmit}>Comment</button>
        </div>
      </div>
    </div>
  );
}

const styles = {
  container: {
    backgroundColor: '#121212',
    color: '#e0e0e0',
    fontFamily: 'Arial, sans-serif',
    padding: '20px',
    minHeight: '100vh',
  },
  title: {
    textAlign: 'center',
    marginBottom: '20px',
  },
  newPost: {
    backgroundColor: '#1e1e1e',
    padding: '15px',
    borderRadius: '8px',
    marginBottom: '30px',
  },
  input: {
    width: '100%',
    padding: '10px',
    marginBottom: '10px',
    borderRadius: '4px',
    border: '1px solid #333',
    backgroundColor: '#333',
    color: '#e0e0e0',
  },
  textarea: {
    width: '100%',
    padding: '10px',
    marginBottom: '10px',
    borderRadius: '4px',
    border: '1px solid #333',
    backgroundColor: '#333',
    color: '#e0e0e0',
    height: '80px',
  },
  button: {
    padding: '10px 15px',
    backgroundColor: '#6200ee',
    color: '#ffffff',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
  },
  posts: {
    marginTop: '20px',
  },
  post: {
    backgroundColor: '#1e1e1e',
    padding: '15px',
    borderRadius: '8px',
    marginBottom: '20px',
  },
  postTitle: {
    marginBottom: '10px',
    fontSize: '1.2em',
  },
  postContent: {
    marginBottom: '15px',
  },
  comments: {
    marginTop: '20px',
  },
  commentsTitle: {
    marginBottom: '10px',
  },
  comment: {
    backgroundColor: '#333',
    padding: '10px',
    borderRadius: '4px',
    marginBottom: '10px',
  },
  newComment: {
    marginTop: '10px',
  },
  noPosts: {
    textAlign: 'center',
  },
};

export default Forum;
