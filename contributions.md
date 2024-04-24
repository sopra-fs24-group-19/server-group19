# Contributions
## BACKEND CONTRIBUTIONS
### Francesco Manzionna
#### Week 1 (26.03 - 09.04)
* Date: 08/04/2024
* https://github.com/orgs/sopra-fs24-group-19/projects/1?pane=issue&itemId=57052158
* https://github.com/orgs/sopra-fs24-group-19/projects/1/views/1?pane=issue&itemId=57054747
* Implemented the BE US1, BE US2 as for the requirements.

#### Week 2 (09.04 - 16.04)
* GitHub issues worked on: #29, #32, #36
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/1?pane=issue&itemId=57052401
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/1?pane=issue&itemId=57052373
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/1?pane=issue&itemId=57052268
* Additional details:
I've implemented Create, Get, and Delete functions to manage user reviews. Initially, user objects included all reviews internally to minimize backend requests. However, due to a preference for dedicated services, I've set up a GET service at URI: /users/{userId}. I haven't yet implemented checks to prevent fraudulent reviews.
Additionally, I've structured the Applications table to facilitate an inner join between tasks and users, ensuring all user applications are consolidated in one dedicated table.

#### Week 3 (16.04 - 23.04)
* GitHub issues worked on: #33
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/1?pane=issue&itemId=57052337
* Additional details:
Research work to implement real-time collaborative document editing (to implement a todo list) focusing on WebSockets for live, bidirectional communication.


### Dana Rapp
#### Week 1 (26.03 - 09.04)
* GitHub issues worked on: BE US1, BE US3
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/1?pane=issue&itemId=57052445
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/1?pane=issue&itemId=57054287
*  further activities: added integration tests and unit tests for all completed tasks
*  task description:
    * Set up the environment, database and templates for all neccessary classes, implemented all neccessary DTOs and part of the DTO Mappers.
    * Included and mapped all columns in the database, established relationships between the tables in the database.
    * Implemented the endpoint for creating a new task and integrated the endpoint with the frontend by adjusting all neccessary fields.

#### Week 2 (09.04 - 16.04)
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

#### Week 3 (17.04 - 23.04)
* GitHub issues worked on: #51, #47
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/4?pane=issue&itemId=60050347
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/4?pane=issue&itemId=60594594
    * further activities: Fixed some bugs and did some code refactoring, deployed the server to google cloud
  *  task description:
    * Implemented endpoint for retrieving an application that a user has sent
    * Implemented endpoint for marking task as done (task is marked as done only when both helper and creator have confirmed)
    

    
## FRONTEND CONTRIBUTIONS
* Name: Nina Rubesa (For further details, please read the comment section in the links)
* Week 1 - Github issues worked on:
    * Setting up environment: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?pane=issue&itemId=57051818
    * FE US1: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?pane=issue&itemId=57054652
* Week 2 - Github issues worked on:
    * FE US4: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?pane=issue&itemId=57051821
    * FE US5: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?pane=issue&itemId=57051824
* Week 3 - Github issues worked on:
    * FE US4 (API): https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?pane=issue&itemId=60169210
    * Task API setup for frontend - searching which one is more suitable for our project: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?pane=issue&itemId=60169481
    * Further work: connection of front and backend, changing some minor visual details in HomeFeed.tsx, UserProfile.tsx
* Week 4 - Github issues worked on:
   * FE US4 (API, adjusting filtering, styling): https://github.com/sopra-fs24-group-19/client-group19/issues/13
       

* Name: Sina Klerings (For further details, please read the comment section in the links)
* Week 1 - Github issues worked on:
    * FE US2: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?filterQuery=repo%3A%22sopra-fs24-group-19%2Fclient-group19%22+type-of-issue%3ATask+week%3A%22Week+1%22&pane=issue&itemId=57051828
    * FE US3: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?filterQuery=repo%3A%22sopra-fs24-group-19%2Fclient-group19%22+type-of-issue%3ATask+week%3A%22Week+1%22&pane=issue&itemId=57051820
* Week 2 - Github issues worked on:
    * FE US5: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?filterQuery=repo%3A%22sopra-fs24-group-19%2Fclient-group19%22+type-of-issue%3ATask+week%3A%22Week+2%22&pane=issue&itemId=57051822
    * FE US10: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?filterQuery=repo%3A%22sopra-fs24-group-19%2Fclient-group19%22+type-of-issue%3ATask+week%3A%22Week+2%22&pane=issue&itemId=57051826
* Week 3 - Github issues worked on:
    * FE US2 (further extension using an autocompletion API): https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?filterQuery=repo%3A%22sopra-fs24-group-19%2Fclient-group19%22+type-of-issue%3ATask+week%3A%22Week+3%22&pane=issue&itemId=60168398
    * FE US3 (further extension using an autocompletion API): https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?filterQuery=repo%3A%22sopra-fs24-group-19%2Fclient-group19%22+type-of-issue%3ATask+week%3A%22Week+3%22&pane=issue&itemId=60167986
    * Further work: removal of mock data, connection of front- and backend as well as the navigation between frontend pages
