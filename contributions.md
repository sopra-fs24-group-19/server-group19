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

#### Week 4 (23.04 - 30.04)
* GitHub issues worked on: #57, #58
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/4?pane=issue&itemId=61233390
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/4?pane=issue&itemId=61233480
* Additional details:
Fixed minor functionalities such as deleting all applications when a user is selected as an helper.

#### Week 5 (31.04 - 07.05)
* GitHub issues worked on: #69, #70
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/4?pane=issue&itemId=61903991
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/4?pane=issue&itemId=61904035
* Additional details:
    * Several controls have been implemented to ensure the legitimacy of reviews and prevent fraudulent behavior:
        * A user can only review another user if they have previously collaborated on a task or assisted the other user in completing a task.
        * It is verified that after the completion of a task, a user can leave only one review for the other user.
        * It is not permitted for a user to review themselves, in order to prevent fraud.
    * Additionally, the Delete TODO endpoint has been redesigned: it now only accepts the ID of the todo and the token, enhancing security and clarity.

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

#### Week 4 (24.04 - 30.04)
* GitHub issues worked on: #54, #55, #56
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/4?pane=issue&itemId=61203545
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/4?pane=issue&itemId=61207387
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/4?pane=issue&itemId=61224532
*  task description:
    * Added a database table for storing to-do items
    * Implemented endpoint for adding a new to-do, including authorization check
    * Implemented endpoint for updating an existing to-do (updating its status and description)

#### Week 5 (31.04 - 07.05)
* GitHub issues worked on: #59, #60, #61
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/4?pane=issue&itemId=61447620
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/4?pane=issue&itemId=61447784
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/4?pane=issue&itemId=61448066
* task description:
  * Modified the authorization for updating tasks such that:
     * the description of a todo can be updated only by the author of the todo 
     *  the helper cannot update the status of any todo (no matter who posted it)
     * the help seeker (creator of the task the todo belongs to) can update the status of both todos that he posted and that the helper posted
  * Implemented endpoint checking whether all to-do items related to a specific task have been marked as done
  * Modified the create task function so that when creating a task, also a default to-do item is created

    
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
   * FE US5: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?pane=issue&itemId=61231454
   * FE US8: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/1?pane=issue&itemId=61231223
   * Deleting a task you posted: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?pane=issue&itemId=61231607
   * Displaying the rest of the coins when you are creating a task
   * User-friendly message when there are no tasks in your radius
   
       

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
* Week 4 - Joker week -> no contributions, only minor bug fixes and adjustments
