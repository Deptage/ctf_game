# API Documentation

This document provides an overview of the available API endpoints for the **home_screen**. These endpoints handle user-related operations, including authentication and user management.

## Table of Contents
- [Users](#users)
    - [GET /users/me](#get-usersme)
    - [GET /users](#get-users)
- [Auth](#auth)
    - [POST /signup](#post-authsignup)
    - [POST /login](#post-authlogin)
- [Instances](#instances)
    - [GET /instances/status](#get-instancesstatus)
    - [GET /instances/runLevel](#get-instancesrunlevel)
    - [GET /instances/stopLevel](#get-instancesstoplevel)
- [Flags](#flag)
  - [GET /flag/solvedInfo](#get-flagsolvedinfo)
  - [POST /flag/submit/{flagId}](#post-flagsubmitflagid)
- [Hints](#hints)
    - [GET /hints](#get-hints)
    - [GET /hints/flag/{flagId}](#get-hintsflagflagid)
    - [POST /hints/flag/{flagId}/reveal](#post-hintsflagflagidreveal)
- [Mails]()
- [Error Handling](#error-handling)
---

## Users


### GET `/users/me`

Fetches the authenticated user's information.

**Response:**
- **200 OK**: Returns the details of the authenticated user.
- **403 Forbidden**: The user is not authenticated.
- **401 Unauthorized**: JWT token is invalid.
- **400 Bad Request**: Invalid form data.

**Example response:**
```json
{
  "username": "john_doe",
  "password": "$2a$10$AJuYqDZci0VMAtn9f8LEdeR.pNLTA9.53WhzSMN6unadbyceuGQkG"
}
```

## Auth

### POST `/auth/signup`

Registers a new user.

**Request body:**
- **CtfgameUserDTO**: The user data to register.

**Example request:**
```bash
POST http://localhost:8080/auth/signup
Content-Type: application/json
{
  "username": "john_doe",
  "password": "password123"
}
```
**Response:**
- **200 OK**: Returns the details of the newly registered user.
- **400 Bad Request**: The user data is invalid.

**Example response:**
```json
{
  "username": "john_doe",
  "password": "password123"
}
```
### POST `/auth/login`

Authenticates a user and returns a JWT token.

**Request body:**
- **CtfgameUserDTO**: The login credentials of the user.

**Example request:**
```bash
POST http://localhost:8080/auth/login
Content-Type: application/json
{
  "username": "john_doe",
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

## Instances

### GET `/instances/status`

Fetches the status of the currently running level instance.

#### Response:
- **200 OK**: Returns the data related to the running level instance.
- **400 Bad Request**: No level is currently running.
- **401 Unauthorized**: JWT token is invalid.
- **403 Forbidden**: User is not authenticated.


#### Example Request:
```bash
GET http://localhost:8080/instances/status
```

**Authorization header with JWT token is needed**

#### Example Response:
```json
{
  "backendPort": 40267,
  "frontendPort": 45163,
  "username": "xd",
  "createdAt": "2024-09-21T12:22:21.440119",
  "secondsRemaining": 3385
}
```

---

### GET `/instances/runLevel`

Starts a new instance of a specified level.

#### Request Parameters:
- **levelName**: (String) The name of the level to run. Valid values: `"Messenger"`, `"Bank"`, `"Forum"`, `"Company"`.

#### Response:
- **200 OK**: The level instance was successfully started.
- **400 Bad Request**: Invalid level name or level already running.
- **401 Unauthorized**: JWT token is invalid.
- **403 Forbidden**: User is not authenticated.


#### Example Request:
```bash
GET http://localhost:8080/instances/runLevel?levelName=Messenger
```

**Authorization header with JWT token is needed**

#### Example Response:
```json
{
  "URL": "http://localhost:5001"
}
```

---

### GET `/instances/stopLevel`

Stops the currently running instance of a level.

#### Response:
- **200 OK**: The level instance was successfully stopped.
- **400 Bad Request**: No level is currently running.
- **401 Unauthorized**: JWT token is invalid.
- **403 Forbidden**: User is not authenticated.

#### Example Request:
```bash
GET http://localhost:8080/instances/stopLevel
```

**Authorization header with JWT token is needed**

#### Example Response:
```json
{
  "message": "Level stopped"
}
```
---
## Flag

### GET `/flag/solvedInfo`

Fetches the information about which flags have been solved by the user.

#### Response:
- **200 OK**: Returns the information about which flags have been solved by the user.

#### Example response:
```json
[
  {
    "id": 1,
    "solved": true
  },
  {
    "id": 2,
    "solved": false
  },
  {
    "id": 3,
    "solved": false
  },
  {
    "id": 4,
    "solved": false
  }
]
```

### Sample flags

The following are the sample flags in the system:
- flag{bank_flag_1}
- flag{bank_flag_2}
- flag{forum_flag}
- flag{company_flag}

### POST `/flag/submit/{flagId}`

Submits a flag for evaluation.

#### Request body:
- **FlagInput**: The flag to submit.

#### Example request:
```bash
POST http://localhost:8080/flag/submit/1
Content-Type: application/json
{
  "flag": "flag{bank_flag_1}"
}
```

#### Response:
- **200 OK**: The flag was successfully submitted and evaluated.
- **404 Not found**: The flag of a given id doesn't exist.

#### Example responses:
```json
{
  "correct": true,
  "message": "Correct! Slay!"
}
```
```json
{
  "correct": false,
  "message": "Incorrect!"
}
```
```json
{
  "correct": false,
  "message": "You have already completed this level!"
}
```
---
## Hints

### GET `/hints`

Fetches all the hints for all the levels. Titles and contents of unread hints are nullified.

#### Response:
- **200 OK**: Returns the hints for all levels.

#### Example response:
```json
[
  {
    "title": null,
    "content": null,
    "flagId": 1,
    "ordinal": 1,
    "pointsCost": 100,
    "revealed": false
  },
  {
    "title": null,
    "content": null,
    "flagId": 1,
    "ordinal": 2,
    "pointsCost": 200,
    "revealed": false
  },
  {
    "title": null,
    "content": null,
    "flagId": 1,
    "ordinal": 3,
    "pointsCost": 300,
    "revealed": false
  },
  {
    "title": null,
    "content": null,
    "flagId": 2,
    "ordinal": 1,
    "pointsCost": 100,
    "revealed": false
  },
  {
    "title": null,
    "content": null,
    "flagId": 2,
    "ordinal": 2,
    "pointsCost": 200,
    "revealed": false
  }
]
```
---

### GET `/hints/flag/{flagId}`

Fetches all the hints for a specific flag. Titles and contents of unread hints are nullified.

#### Path variables:
- **flagId**: The name of the flag to fetch hints for.

#### Response:
- **200 OK**: Returns the hints for the specified flag.

#### Example response:
```json
[
  {
    "title": null,
    "content": null,
    "flagId": 1,
    "ordinal": 1,
    "pointsCost": 100,
    "revealed": false
  },
  {
    "title": null,
    "content": null,
    "flagId": 1,
    "ordinal": 2,
    "pointsCost": 200,
    "revealed": false
  },
  {
    "title": null,
    "content": null,
    "flagId": 1,
    "ordinal": 3,
    "pointsCost": 300,
    "revealed": false
  }
]
```

### POST `/hints/flag/{flagId}/reveal`

Reveals a hint for a specific flag.

#### Path variables:
- **flag**: The id of the flag to reveal the hint for.

#### Response:
- **200 OK**: The hint was successfully revealed.
- **404 Not found**: There are no more hints to reveal.

#### Example request:
```bash
POST http://localhost:8080/hints/flag/1/reveal
```

#### Example response:
```json
{
  "title": "SQLin again",
  "content": "Use SQLin again to get the flag",
  "flagId": 1,
  "ordinal": 3,
  "revealed": true
}
```
```json
{
  "message": "Hint not found",
  "status": 404
}
```
---
## Mail

### GET `/mail`

Fetches all mail items visible to the currently authenticated user.

#### Response:
- **200 OK**: Returns a list of mail items visible to the user.

#### Example Request:
```bash
GET http://localhost:8080/mail
```

**Authorization header with JWT token is needed**

#### Example Response:
```json
[
  {
    "id": 1,
    "topic": "Hack the bank!",
    "content": "Alright, listen up! The SIGSEGV National Bank was revealed to have insiders from Reverseland. And it seems that they left a few vulnerabilities... Do what you must do with that info, but it's strictly confidential.",
    "sender": "Rohtened Nrogara",
    "read": false
  },
  {
    "id": 2,
    "topic": "help with the forum",
    "content": "Can you help to fix the forum? I accidentally sent some photos nobody should see, and I don't want it leaked",
    "sender": "Noruas Nedoeht",
    "read": false
  },
]
```

### PUT `/mail/{mailId}/read`

Marks a mail item as read.

#### Path variables:
- **mailId**: The id of the mail item to mark as read.

#### Response:
- **200 OK**: The mail item was successfully marked as read.
- **404 Not found**: The mail item of a given id doesn't exist or the mail isn't visible to the user.

#### Example Request:
```bash
POST http://localhost:8080/mail/1/read
```

**Authorization header with JWT token is needed**

---

## Error Handling

**Any error message follows the format:**
```json
{
  "message": "JWT token expired",
  "status": 401
}
```
