# API Documentation

This document provides an overview of the available API endpoints for the **forum**. These endpoints handle operations related to posts and comments.

## Table of Contents
- [Auth](#auth)
  - [POST /signup](#post-authsignup)
  - [POST /login](#post-authlogin)
- [Posts](#post-posts)
    - [GET /posts](#get-posts)
    - [GET /posts/{postId}](#get-postspostid)
- [Flag](#flag)
    - [GET /flag](#get-flag)
- [Default users](#default-users)
- [Error Handling](#error-handling)

---
## Auth

### POST `/auth/signup`

Registers a new user.

**Request body:**
- **ForumUserDTO**: The user data to register.

**Example request:**
```bash
POST http://localhost:8080/auth/signup
Content-Type: application/json
{
  "username": "xd",
  "password": "xd"
}
```
**Response:**
- **200 OK**: Returns the details of the newly registered user.
- **400 Bad Request**: The user data is invalid.

**Example response:**
```json
{
  "username": "xd",
  "roles": "USER"
}
```
---
### POST `/auth/login`

Authenticates a user.

**Request body:**
- **ForumUserDTO**: The login credentials of the user.

**Example request:**
```bash
POST http://localhost:8080/auth/login
Content-Type: application/json
{
  "username": "user",
  "password": "user"
}
```
**Response:**
- **200 OK**: Returns the JWT token and expiration time.
- **400 Bad Request**: The login credentials are invalid.
- **404 Not found**: Bad credentials.

**Example response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 3600
}
```
---

### POST `/auth/logout`

Deletes the role cookie.

**Response:**
- **200 OK**: Deletes the role cookie.

---
## Posts

### GET `/posts`

Fetches a list of all posts. Requires cookie role=ROLE_ADMIN.

**Response:**
- **200 OK**: Returns an array of posts.
- **400 Bad Request**: Cookie is not present.

**Example response:**
```json
[
  {
    "id": 1,
    "title": "Post Title",
    "content": "Post content...",
    "comments": [],
    "author": {
      "username": "user",
      "roles": "USER",
      "postsCount": 2,
      "commentsCount": 1
    }
  },
  {
    "id": 2,
    "title": "Another Post Title",
    "content": "More post content...",
    "comments": [
      {
        "content": "Great post!",
        "author": {
          "username": "jane_doe",
          "roles": "USER"
        }
      }
    ],
    "author": {
      "username": "user",
      "roles": "USER",
      "postsCount": 2,
      "commentsCount": 1
    }
  }
]
```
### GET `/posts/{postId}`

Fetches a specific post by its ID.

**Path parameters:**
- `postId` (required): The ID of the post to retrieve.

**Response:**
- **200 OK**: Returns the details of the specified post.
- **404 NOT FOUND**: If the post with the specified ID does not exist.

**Example response:**
```json
{
  "id": 1,
  "title": "Post Title",
  "content": "Post content...",
  "comments": [
    {
      "content": "Great post!",
      "author": {
        "username": "user",
        "roles": "USER"
      }
    }
  ],
  "author": {
    "username": "user",
    "roles": "USER",
    "postsCount": 2,
    "commentsCount": 1
  }
}
```
---
## Flag

### GET `/flag`
Fetches the level flag. Requires cookie role=ROLE_ADMIN.

**Response:**
- **200 OK**: Returns the flag.

**Example response:**
```json
{
  "flag": "flag{this_is_a_flag}"
}
```

---
## Default users

The following default users are available in the system:
- User: username = `user`, password = `user`
- Admin: username = `admin`, password = `admin`

These users can be used to test the API endpoints.

--- 
## Error Handling

**Any error message follows the format:**
```json
{
  "message": "Username cannot be null",
  "status": 400
}
```