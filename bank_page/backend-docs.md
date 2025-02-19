# API Documentation

This document provides an overview of the available API endpoints for the **bank**. These endpoints handle user-related operations, including authentication and user management, as well as account and transaction management.

## Table of Contents
- [Users](#users)
  - [GET /users/me](#get-usersme)
  - [GET /users](#get-users)
- [Auth](#auth)
  - [POST /signup](#post-authsignup)
  - [POST /login](#post-authlogin)
  - [GET /exists](#get-authexists)
- [Accounts](#accounts)
  - [GET /accounts](#get-accounts)
  - [GET /accounts/me](#get-accountsme)
  - [POST /accounts](#post-accounts)
- [Transactions](#transactions)
  - [GET /transactions/searchByTitle](#get-transactionssearchbytitle)
  - [POST /transactions](#post-transactions)
- [Default users](#default-users)
- [Error Handling](#error-handling)
---

## Users


### GET `/users/me`

Fetches the authenticated user's information.

**Response:**
- **200 OK**: Returns the details of the authenticated user.
- **403 Forbidden**: The user is not authenticated.

**Example response:**
```json
{
  "username": "john_doe",
  "password": "$2a$10$AJuYqDZci0VMAtn9f8LEdeR.pNLTA9.53WhzSMN6unadbyceuGQkG"
}
```
### GET `/users`

Fetches the list of all users.

**Response:**
- **200 OK**: Returns an array of all users.
- **403 Forbidden**: The user is not authorised as ADMIN or not authenticated.

**Example response:**
```json
[
  {
    "username": "john_doe",
    "password": "$2a$10$AJuYqDZci0VMAtn9f8LEdeR.pNLTA9.53WhzSMN6unadbyceuGQkG"
  },
  {
    "username": "jane_doe",
    "password": "$2a$10$AJuYqDZci0VMAtn9f8LEdeR.pNLTA9.53WhzSMN6unadbyceuGQkG"
  }
]
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
  "password": "$2a$10$vPWYH6oWBp6QfSEHqRy5WuJw1rLZgYsW6FaI4R21OATAZvRZ98eLO",
  "accounts": [],
  "roles": [
    {
      "name": "ROLE_USER"
    }
  ]
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

### GET `/auth/exists`

Checks if a user exists.

Request params:
- **username**: The username to check for existence.

**Example request:**
```bash
GET http://localhost:8080/auth/exists?username=john_doe
```

**Response:**
- **200 OK**: Returns whether the user exists.

**Example responses:**
```json
{
  "exists": true
}
```

## Accounts

### GET `/accounts`

Fetches the list of all accounts.

**Response:**
- **200 OK**: Returns an array of all accounts.
- **403 Forbidden**: The user is not authorised as ADMIN or not authenticated.

**Example responses:**
```json
[
  {
    "id": 1,
    "accountNumber": "125941392181",
    "balance": 90.0,
    "createdAt": "2024-09-15T02:15:27.69696",
    "userId": 2,
    "sourceTransactions": [
      {
        "id": 1,
        "amount": 10.0,
        "createdAt": "2024-09-15T02:16:54.081975",
        "sourceAccountId": 1,
        "destinationAccountId": 2
      }
    ],
    "destinationTransactions": []
  },
  {
    "id": 2,
    "accountNumber": "318123311939",
    "balance": 10.0,
    "createdAt": "2024-09-15T02:15:28.595759",
    "userId": 2,
    "sourceTransactions": [],
    "destinationTransactions": [
      {
        "id": 1,
        "amount": 10.0,
        "createdAt": "2024-09-15T02:16:54.081975",
        "sourceAccountId": 1,
        "destinationAccountId": 2
      }
    ]
  }
]
```

### GET `/accounts/me`

Fetches the list of all accounts of the authenticated user.

**Response:**
- **200 OK**: Returns an array of all accounts of the authenticated user.
- **403 Forbidden**: The user is not authenticated.

**Example responses:**
```json
[
  {
    "id": 1,
    "accountNumber": "125941392181",
    "balance": 90.0,
    "createdAt": "2024-09-15T02:15:27.69696",
    "userId": 2,
    "sourceTransactions": [],
    "destinationTransactions": []
  },
  {
    "id": 2,
    "accountNumber": "318123311939",
    "balance": 10.0,
    "createdAt": "2024-09-15T02:15:28.595759",
    "userId": 2,
    "sourceTransactions": [],
    "destinationTransactions": []
  }
]
```

### POST `/accounts`

Creates a new account for the authenticated user.

**Request params:**
- **initBalance** (optional): The initial balance of the account. If not set, the default value is 100.0.

**Example request:**
```bash
POST http://localhost:8080/accounts?initBalance=200.0
```

**Response:**
- **200 OK**: Returns the details of the newly created account.
- **403 Forbidden**: The user is not authenticated.

**Example responses:**
```json
{
  "id": 1,
  "accountNumber": "125941392181",
  "balance": 200.0,
  "createdAt": "2024-09-15T02:15:27.69696",
  "userId": 2,
  "sourceTransactions": [],
  "destinationTransactions": []
}
```

## Transactions

### GET `/transactions/searchByTitle`

Fetches the list of all transactions whose title contains a given text.

Request params:
- **title**: The text to search for in the transaction title.

An empty title will return all transactions.

**Example request:**
```bash
GET http://localhost:8080/transactions/searchByTitle?title=transfer
```

**Response:**
- **200 OK**: Returns an array of all transactions whose title contains the given text.
- **403 Forbidden**: The user is not authenticated.

**Example responses:**
```json
[
  {
    "createdAt": "2024-10-22 03:13:03",
    "amount": "$10.00",
    "sourceAccountNumber": "623853276346",
    "destinationAccountNumber": "723396779515",
    "title": "XD XD"
  },
  {
    "createdAt": "2024-10-22 03:13:07",
    "amount": "$10.00",
    "sourceAccountNumber": "623853276346",
    "destinationAccountNumber": "723396779515",
    "title": "XD XD"
  }
]
```

### POST `/transactions`

Creates a new transaction between two accounts.

**Request body:**
- **TransactionDTO**: The transaction data.

**Example request:**
```bash
POST http://localhost:8080/transactions
Content-Type: application/json
{
  "sourceAccountNumber": "123",
  "destinationAccountNumber": "456",
  "title": "transfer",
  "amount": 10.0
}
```
A null title will be replaced with "transfer".  

**Response:**
- **200 OK**: Returns the details of the newly created transaction.
- **400 Bad Request**: The transaction amount is invalid.
- **403 Forbidden**: The user is not authenticated.
- **404 Not Found**: The source or destination account does not exist.

**Example responses:**
```json
{
  "id": 1,
  "amount": 10.0,
  "createdAt": "2024-09-15T02:16:54.081975",
  "sourceAccountNumber": "123",
  "destinationAccountNumber": "456"
}
```
## Default users

The following default users are available for testing:

- **Username**: `admin`, **Password**: `@@cde3$RFV@@`
- **Username**: `user`, **Password**: `dragon`

## Error Handling

**Any error message follows the format:**
```json
{
  "message": "Amount must be greater than 0",
  "status": 400
}
```
