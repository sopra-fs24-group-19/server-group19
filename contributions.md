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
 
#### Week 6 (08.05 - 15.05)
* GitHub issues worked on: #31, #32 (both frontend)
    * https://github.com/sopra-fs24-group-19/client-group19/issues/31
    * https://github.com/sopra-fs24-group-19/client-group19/issues/32
* Additional details:
    * Enhancement of User Detail Page:
        * The user detail page has been updated to now display for each review the author, creation date, star rating, and comment. Additionally, clicking on the author's name of each review will display their profile. The SCSS has also been modified to make the appearance more visually appealing.
    * General frontend overhaul:
        * Fixed a bug that caused the server to crash; now, each GET request for updates is sent every two seconds instead of every millisecond.
        * Replaced hrefs with links for some redirections to leverage the single-page application concept of React, avoiding page reloads and making the application noticeably more responsive.
        * Attempted to fix the logo by changing the image path; it is not yet working in the deployed version, suspecting that the build file has not been correctly switched on Google Cloud.
        * Fixed the favicon, which was not being displayed correctly.

#### Week 7 (15.05 - 21.05)
* GitHub issues worked on: #42 (frontend), #43 (frontend), #90 (backend)
    * https://github.com/sopra-fs24-group-19/client-group19/issues/42
    * https://github.com/sopra-fs24-group-19/client-group19/issues/43
    * https://github.com/sopra-fs24-group-19/server-group19/issues/90
* Additional details:
    * Bug fix: when a user closed the session improperly (without logging out first), the token remained in local storage. This led to numerous errors when the user reconnected to the web app.
    * Reformatted all fields where a date was displayed with dayJS.
    * Backend: implemented endpoint for frontend to validate token and associate with user id.


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
* GitHub issues worked on: #59, #60, #61, #67, #72
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/4?pane=issue&itemId=61447620
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/4?pane=issue&itemId=61447784
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/4?pane=issue&itemId=61448066
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/4?pane=issue&itemId=61825298
    * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/4?pane=issue&itemId=62175719
* task description:
  * #59: Modified the authorization for updating tasks such that:
     *  the description of a todo can be updated only by the author of the todo 
     *  the helper cannot update the status of any todo (no matter who posted it)
     * the help seeker (creator of the task the todo belongs to) can update the status of both todos that he posted and that the helper posted
  * #60: Implemented endpoint checking whether all to-do items related to a specific task have been marked as done
  * #61: Modified the create task function so that when creating a task, also a default to-do item is created (this has been changed later as we decided to handle this differently. Instead of creating a default todo upon task creation, I added a check to the allTodosDone method so that it returns false if there have never been any todos associated with the given task)
  * #67: Implemented a new endpoint that allows the frontend to retrieve a task using its id
  * #72: overall code refactoring (fixed some dependency issues due to cross-calling the repositories from different services)
*  further activities: Fixed some bugs, deployed the server to Google cloud, wrote tests for the new features

#### Week 6 (07.05 - 14.05)
* GitHub issues worked on: #24, #25, #71
  * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?pane=issue&itemId=62164588
  * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?pane=issue&itemId=62174535
  * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/4?pane=issue&itemId=62134907
* task descriptions:
  * #24 (Frontend): Created a page for displaying a user ranking based on how many tasks they have been completed
  * #25 (Frontend): Added a button to the header that redirects the user to the leaderboard page
  * #71 (Backend): Implemented a new get endpoint that returns a list of users sorted by the number of tasks they have completed, along with their rank and the number of completed tasks

#### Week 7 (14.05 - 20.05)
* GitHub issues worked on: #38, #40, #86
 * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/1?pane=issue&itemId=63056530
 * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/1?pane=issue&itemId=63150584
 * https://github.com/orgs/sopra-fs24-group-19/projects/1/views/1?pane=issue&itemId=63151163
* task descriptions:
    * #38 (frontend): Created two filter options on the homefeed and implemented filtering of the shown tasks according to the user-specified filters
    * #40 (frontend): Created buttons for leaving a review on myTasks and myApplications. The buttons are only rendered after the current user has marked the task as done, and only clickable if the user has not reviewed the task already
    * #86 (backend): Created an endpoint for checking whether a task has already been reviewed by the user (used for conditional rendering in #40)
  

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
    * FE User Profile adjustments: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?visibleFields=%5B%22Title%22%2C%22Assignees%22%2C%22Status%22%2C84598672%5D&pane=issue&itemId=63792913
    * Further work: connection of front and backend, changing some minor visual details in HomeFeed.tsx
* Week 4 - Github issues worked on:
   * FE US4 (API, adjusting filtering, styling): https://github.com/sopra-fs24-group-19/client-group19/issues/13
   * FE US5: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?pane=issue&itemId=61231454
   * FE US8: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/1?pane=issue&itemId=61231223
   * Deleting a task you posted: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?pane=issue&itemId=61231607
   * Displaying the rest of the coins when you are creating a task
   * User-friendly message when there are no tasks in your radius
* Week 5 - Github issues worked on: #20, #23
    * ToDo List - functionality: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?pane=issue&itemId=61800385
    * ToDo List - making a template and connecting it to other pages: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?pane=issue&itemId=61900653
* Week 6 - Github issues worked on: #35, #33, #34, #36
    * ToDo - Delete: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?groupedBy%5BcolumnId%5D=Assignees&sortedBy%5Bdirection%5D=asc&sortedBy%5BcolumnId%5D=84598672&pane=issue&itemId=62630787
    * ToDo - Task Done - Status update: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?groupedBy%5BcolumnId%5D=Assignees&sortedBy%5Bdirection%5D=asc&sortedBy%5BcolumnId%5D=84598672&pane=issue&itemId=62630852
    * ToDo - user friendly message: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?groupedBy%5BcolumnId%5D=Assignees&sortedBy%5Bdirection%5D=asc&sortedBy%5BcolumnId%5D=84598672&pane=issue&itemId=62631059
    * Leaving review - adjustment: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?groupedBy%5BcolumnId%5D=Assignees&sortedBy%5Bdirection%5D=asc&sortedBy%5BcolumnId%5D=84598672&pane=issue&itemId=62630966
* Week 7 - Github issues worked on: #39
    * Guards: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?pane=issue&itemId=63138970 
   
       

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
* Week 5 - Github issues worked on:
    * TODO List - design: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?pane=issue&itemId=61801048
    * Show users information in the header: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?pane=issue&itemId=61801252
    * Note: The commit noted in the issues contains a lot more bug fixes, design fixes and extension, for more details, please visit the commit
* Week 6 - Github issues worked on:
  * Adding automatic page refreshes throughout the website: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?filterQuery=repo%3A%22sopra-fs24-group-19%2Fclient-group19%22+type-of-issue%3ATask++week%3A%22Week+6%22++assignee%3A%40me&pane=issue&itemId=62603939
  * Make the website more user-friendly: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?filterQuery=repo%3A%22sopra-fs24-group-19%2Fclient-group19%22+type-of-issue%3ATask++week%3A%22Week+6%22++assignee%3A%40me&pane=issue&itemId=62603833
  * Add Pop-up windows for usability: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?filterQuery=repo%3A%22sopra-fs24-group-19%2Fclient-group19%22+type-of-issue%3ATask++week%3A%22Week+6%22++assignee%3A%40me&pane=issue&itemId=62603847
  * Add the option to filter tasks in MyTasks and MyApplication: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?filterQuery=repo%3A%22sopra-fs24-group-19%2Fclient-group19%22+type-of-issue%3ATask++week%3A%22Week+6%22++assignee%3A%40me&pane=issue&itemId=62604036
* Week 7 - Github issues worked on:
  * Enhanced the review at a later point in time: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?filterQuery=repo%3A%22sopra-fs24-group-19%2Fclient-group19%22+type-of-issue%3ATask++week%3A%22Week+7%22++assignee%3A%40me&pane=issue&itemId=63665063
  * Disable ability to add new ToDo's after one party has closed the task: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?filterQuery=repo%3A%22sopra-fs24-group-19%2Fclient-group19%22+type-of-issue%3ATask++week%3A%22Week+7%22++assignee%3A%40me&pane=issue&itemId=63911327
  * Experiment how to get logo and default profile picture on the deployed version: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?filterQuery=repo%3A%22sopra-fs24-group-19%2Fclient-group19%22+type-of-issue%3ATask++week%3A%22Week+7%22++assignee%3A%40me&pane=issue&itemId=63915009
  * Disable users from creating tasks for a past hour: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?filterQuery=repo%3A%22sopra-fs24-group-19%2Fclient-group19%22+type-of-issue%3ATask++week%3A%22Week+7%22++assignee%3A%40me&pane=issue&itemId=63915009
  * Make website accessible from the phone: https://github.com/orgs/sopra-fs24-group-19/projects/1/views/3?filterQuery=repo%3A%22sopra-fs24-group-19%2Fclient-group19%22+type-of-issue%3ATask++week%3A%22Week+7%22++assignee%3A%40me&pane=issue&itemId=63914654
  * Further work: Fixed the loading of the header, fixed filter option in MyApplications, further fixes mentioned in the commits to finalize the web page