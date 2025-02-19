# API Documentation

This document provides an overview of the available API endpoints for the **company**. These endpoints handle operations related to worker data.

## Table of Contents
- [Workers](#workers)
    - [GET /workers](#get-workers)
    - [GET /workers/{workerId}](#get-workersworkerid)
- [Error Handling](#error-handling)
---

## Workers

### GET `/workers`

Fetches a list of all workers.

**Response:**
- **200 OK**: Returns an array of worker details.

**Example response:**
```json
[
  {
    "name": "John Doe",
    "position": "Engineer",
    "year": "2020",
    "description": "Senior software engineer"
  },
  {
    "name": "Jane Smith",
    "position": "Manager",
    "year": "2015",
    "description": "Project manager in charge of multiple teams"
  }
]
```

### GET `/workres/{workerId}`

Fetches the details of a specific worker by their ID.

**Path parameters:**
- `workerId` (required): The ID of the worker to retrieve.

**Response:**
- **200 OK**: Returns the details of the specified worker.
- **404 Not Found**: If the worker with the specified ID does not exist.

**Example response:**
```json
{
  "name": "John Doe",
  "position": "Engineer",
  "year": "2020",
  "description": "Senior software engineer"
}
```

--- 
## Error Handling

**Any error message follows the format:**
```json
{
  "message": "Worker not found",
  "status": 404
}
```