# To-do REST API

This to-do application is simple RESTful API built using Spring Boot. 
It allows you to manage a list of tasks, providing basic functionalities 
like creating, updating, retrieving and deleting to-do items.
The application also features comprehensive RESTful API tests and was developed using
the Test-Driven Development (TDD) approach and utilizes a H2 database.

## Features
* **Retrieve to-do by ID:** You can retrieve a to-do item by specifying its unique ID.
* **Retrieve all to-dos:** Get a list of all to-do items.
* **Pagination Support:** The API supports pagination for efficiently handling large datasets.
* **Create new to-do**: Add new tasks to your ToDo list.
* **Update to-do:** Modify existing tasks with updated information.
* **Delete to-do:** Remove tasks from your ToDo list.


## Usage
### Retrieve to-do by ID
Request:
`GET /todos/{id}`

Response:

    {
        "id": 1,
        "value": "Buy groceries."
    }

### Retrieve all to-dos
Request: `GET /todos`

Response:

    [
        {
            "id": 1,
            "value": "Buy groceries."
        },
        {
            "id": 2,
            "value": "Complete 30 minutes of exercise."
        }
    ]

### Create new to-do
Request: `POST /todos`

    {
        value: "Complete 30 minutes of exercise."
    }

Response: `201 Created
Location: /todos/{new_todo_id}`

### Update to-do

Request: `PUT /todos/{id}`
    
    {
        "value": "Read 15 pages of the book."
    }

Response: `204 No Content`

### Delete to-do

Request: `DELETE /todos/{id}`

Response: `204 No Content`

### Running Tests

This project follows the Test-Driven Development (TDD) approach.
To run the tests, execute the following command: `gradle test`



