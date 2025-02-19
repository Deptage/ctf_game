# API Documentation

This document provides an overview of the available API endpoints for the **aqua messenger**. There endpoints handle user-related operations, including authentication and user management, as well as conversation management.

## Table of Contents
- [Auth](#auth)
  - [POST /auth/login](#post-authlogin)
- [Conversation](#conversation)
  - [GET /conversation](#1-get-conversation)
  - [GET /conversation/{conversationId}/sentMessages](#2-get-conversationconversationidsentmessages)
  - [GET /conversation/{conversationId}/info](#3-get-conversationconversationidinfo)
  - [POST /conversation/{conversationId}/sendNew](#4-post-conversationconversationidsendnew)
  - [POST /conversation/{conversationId}/sendExisting/{messageId}](#5-post-conversationconversationidsendexistingmessageid)
  - [GET /conversation/{conversationId}/nextMessages](#6-get-conversationconversationidnextmessages)

---
## Auth

### POST `/auth/login`

Authenticates a user and returns a JWT token.

**Request body:**
- **UserDTO**: The login credentials of the user.

**Example request:**
```bash
POST http://localhost:8080/auth/login
Content-Type: application/json
{
  "username": "antiquascarlet1697",
  "password": "password123"
}
```
**Response:**
- **200 OK**: Returns the JWT token and expiration time.
- **400 Bad Request**: The login credentials are invalid.
- **401 Unauthorized**: User was not found.

**Example response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 3600
}
```
---
## Conversation

### 1. **GET** `/conversation`

#### Description:
Fetches the list of contacts (other users) that the given user has had conversations with. The contacts are determined from all the conversations the user is a part of.



#### Example Request:
```bash
GET http://localhost:8080/conversation
```

#### Response:
- **200 OK** — A successful response that contains a list of usernames the user has had conversations with.


#### Example Response:
```json
[
  {
    "id": 1,
    "contactName": "Robert Carlson"
  },
  {
    "id": 2,
    "contactName": "Madeleine Currey"
  }
]
```
---

### 2. **GET** `/conversation/{conversationId}/sentMessages`

#### Description:
Retrieves all messages exchanged between two users in a conversation. The conversation is identified by its id.

#### Parameters:
- **conversationId** (required) — The conversation ID

#### Example Request:
```bash
http://localhost:8080/conversation/1/sentMessages
```
#### Response:
- **200 OK** — A successful response that contains the list of messages in the conversation.


#### Example Response:
```json
[
  {
    "id": 1,
    "conversationId": 1,
    "from": "Robert Carlson",
    "content": "Hi Antiqua, have you graduated from the Computer Science Trade School in the capital by any chance?",
    "sentAt": "2024-11-20T19:27:53.880951"
  },
  {
    "id": 3,
    "conversationId": 1,
    "from": "testuser",
    "content": "Hello, who are you?",
    "sentAt": "2024-11-20T19:33:12.480401"
  }
]
```
#### Error Responses:
- **404 Not found** — If the conversation ID is invalid, the following error message is returned.
  ```json
  {
    "message": "Conversation not found with id: 1",
    "status": 404
  }
  ```
---
### 3. **GET** `/conversation/{conversationId}/info`

#### Description:
Retrieves the information about whether the conversation is SCRIPTED or FREE.

#### Parameters:
- **conversationId** (required) — The conversation ID

#### Example Request:
```bash
GET http://localhost:8080/conversation/1/info
```
#### Response:
- **200 OK** — A successful response that contains information about the conversation type.

#### Example Response:
```json
{
  "type": "SCRIPTED"
}
```

#### Error Responses:
- **404 Not found** — If the conversation ID is invalid, the following error message is returned.
  ```json
  {
    "message": "Conversation not found with id: 1",
    "status": 404
  }
  ```

---
### 4. **POST** `/conversation/{conversationId}/sendNew`

#### Description:
Sends a message from one user to another in a conversation. The conversation is identified by its id. Only available if the conversation is of type FREE.

#### Example Request:
```bash
POST http://localhost:8080/conversation/1/sendNew
Content-Type: application/json
{
  "content": "content"
}
```
#### Response:
- **200 OK** — A successful response that contains the message that was sent.

#### Example Response:
```json
{
  "id": 3,
  "conversationId": 1,
  "from": "testuser",
  "content": "Hello, who are you?",
  "sentAt": "2024-11-20T19:33:12.4804013"
}
```

#### Error Responses:
- **400 Bad Request** — If the conversation is of type SCRIPTED, the following error message is returned.
  ```json
  {
    "message": "Cannot create new messages to scripted conversations",
    "status": 400
  }
  ```
---

### 5. **POST** `/conversation/{conversationId}/sendExisting/{messageId}`

#### Description:
Sends a message from one user to another in a conversation and the following bot message if one exists. The conversation is identified by its id. Only available if the conversation is of type SCRIPTED. If after sending the message no next messages are available, the conversation type is changed to FREE.

#### Parameters:
- **conversationId** (required) — The conversation ID
- **messageId** (required) — The message ID

#### Example Request:
```bash
POST http://localhost:8080/conversation/1/sendExisting/3
```

#### Response:
- **200 OK** — A successful response that contains the messages that were sent.

#### Example Response:
```json
[
  {
    "id": 3,
    "conversationId": 1,
    "from": "testuser",
    "content": "Hello, who are you?",
    "sentAt": "2024-11-20T20:19:00.4789749"
  },
  {
    "id": 6,
    "conversationId": 1,
    "from": "Robert Carlson",
    "content": "Oh, sorry to not introduce myself first. My name's Robert Carlson, i'm an inventor and I graduated the CS Trade School three years ago :D",
    "sentAt": "2024-11-20T20:19:00.478994"
  }
]
```

#### Error Responses:
- **400 Bad Request** — If the message is not within the next viable messages for this conversation
  ```json
  {
    "message": "Not a viable next message at this stage",
    "status": 400
  }
  ```
- **400 Bad Request** — If current user is not the participant of the conversation
  ```json
  {
    "message": "User is not the sender of the message",
    "status": 400
  }
  ```
- **404 Not found** — If the conversation ID or message ID is invalid, the following error message is returned.
  ```json
  {
    "message": "Conversation not found with id: 1",
    "status": 404
  }
  ```
- **404 Not found** — If the message ID is invalid, the following error message is returned.
  ```json
  {
    "message": "Message not found with ",
    "status": 404
  }
  ```
---

### 6. **GET** `/conversation/{conversationId}/nextMessages`

#### Description:
Retrieves the next messages that the user can send in a scripted conversation. The conversation is identified by its id.

#### Parameters:
- **conversationId** (required) — The conversation ID

#### Example Request:
```bash
GET http://localhost:8080/conversation/1/nextMessages
```
#### Response:
- **200 OK** — A successful response that contains the list of next messages that the user can send.

#### Example Response:
```json
[
  {
    "id": 10,
    "content": "Hi Robert. Three years ago? Have you found a good job after graduating our school?"
  },
  {
    "id": 11,
    "content": "Hi, nice to meet you. I'm Antiqua Scarlet, but you probably already know that haha"
  }
]
```

#### Error Responses:
- **404 Not found** — If the conversation ID is invalid, the following error message is returned.
  ```json
  {
    "message": "Conversation not found with id: 1",
    "status": 404
  }
  ```

### Summary of Endpoints 

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/auth/login` | Authenticates a user and returns a JWT token. |
| GET | `/conversation` | Fetches the list of contacts (other users) that the given user has had conversations with. |
| GET | `/conversation/{conversationId}/sentMessages` | Retrieves all messages exchanged between two users in a conversation. |
| GET | `/conversation/{conversationId}/info` | Retrieves the information about whether the conversation is SCRIPTED or FREE. |
| POST | `/conversation/{conversationId}/sendNew` | Sends a message from one user to another in a conversation. |
| POST | `/conversation/{conversationId}/sendExisting/{messageId}` | Sends a message from one user to another in a conversation and the following bot message if one exists. |
| GET | `/conversation/{conversationId}/nextMessages` | Retrieves the next messages that the user can send in a scripted conversation. |
---


