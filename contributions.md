## Contributions
# Week 1 (26.03 - 09.04)
* Name: Francesco Manzionna
* Date: 08/04/2024
* https://github.com/orgs/sopra-fs24-group-19/projects/1?pane=issue&itemId=57052158
* https://github.com/orgs/sopra-fs24-group-19/projects/1/views/1?pane=issue&itemId=57054747
* Implemented the BE US1, BE US2 as for the requirements.
---
* Name: Dana Rapp
* GitHub issues worked on: BE US1, BE US3
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/1?pane=issue&itemId=57052445
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/1?pane=issue&itemId=57054287
*  further activities: added integration tests and unit tests for all completed tasks
*  task description:
    * Set up the environment, database and templates for all neccessary classes, implemented all neccessary DTOs and part of the DTO Mappers.
    * Included and mapped all columns in the database, established relationships between the tables in the database.
    * Implemented the endpoint for creating a new task and integrated the endpoint with the frontend by adjusting all neccessary fields.

# Week 2 (09.04 - 16.04)
* Name: Dana Rapp
* GitHub issues worked on: #45, #42, #40, #41, #44
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/4?pane=issue&itemId=59409663
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/4?pane=issue&itemId=59409907
    *  https://github.com/orgs/sopra-fs24-group-19/projects/1/views/4?pane=issue&itemId=59428274
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/4?pane=issue&itemId=59428372
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/4?pane=issue&itemId=59476951
    * further activities: added unit tests for user service, task service, user controller and task controller for all completed tasks 
*  task description:
    * Implemented get endpoints for getting all tasks, getting tasks posted by a specific user and tasks a specific user has applied to
    * Implemented get endpoint for getting candidates who have applied to a specific task
    * Implemented the endpoint getting all tasks the current user has posted
    * Implemented the endpoint for deleting a task the user has posted, including verification whether the user is authorized to delete the task
---
* Name: Francesco Manzionna
* Date: 14/04/2024
* GitHub issues worked on: #29, #32, #36
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/1?pane=issue&itemId=57052401
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/1?pane=issue&itemId=57052373
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/1?pane=issue&itemId=57052268
* Additional details:
I've implemented Create, Get, and Delete functions to manage user reviews. Initially, user objects included all reviews internally to minimize backend requests. However, due to a preference for dedicated services, I've set up a GET service at URI: /users/{userId}. I haven't yet implemented checks to prevent fraudulent reviews.
Additionally, I've structured the Applications table to facilitate an inner join between tasks and users, ensuring all user applications are consolidated in one dedicated table.

# Week 3 (16.04 - 23.04)

* Name: Francesco Manzionna
* Date: 22/04/2024
* GitHub issues worked on: #33
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/1?pane=issue&itemId=57052337
* Additional details:
Research work to implement real-time collaborative document editing (to implement a todo list) focusing on WebSockets for live, bidirectional communication.
---
