# Helping Hands - Reinventing Community Solidarity

In an age where technology dominates our lives, the sense of community in our neighborhoods has sadly diminished. It’s not uncommon today for people to not know the names of their neighbors, with greetings reduced to mere formalities. The Helping Hands project is born out of the necessity to revive that lost sense of community, reminiscent of the times of our parents and grandparents.

## Introduction

Helping Hands aims to recreate the cohesive and supportive neighborhood environment that once existed. A friendly neighbor can significantly simplify daily life, and that is precisely the objective of Helping Hands.

### About the App

Helping Hands is a web application designed to foster neighborly assistance and collaboration. Here’s how it works:

1. **Registration**: Users can sign up and create a profile.
2. **Posting Tasks**: Users can post requests for help with specific tasks, such as grocery shopping, pet sitting, or any other daily activities.
3. **Finding Tasks**: By entering their home address, users can view tasks nearby. This ensures that help is always close at hand.
4. **Choosing Tasks**: Users can select tasks that match their skills and preferences.
5. **Earning Coins**: Upon completing tasks, users earn symbolic coins. These coins incentivize helping others and can be used to post their own tasks.
6. **Community Feedback**: A review system ensures that the individuals offering help are trustworthy and competent.
7. **Leaderboard**: There is a leaderboard where you can see the most helpful people in the community, fostering a spirit of friendly competition and recognition.

### Vision

Our vision is to restore the neighborly spirit of mutual assistance and goodwill. In a world increasingly disconnected by technology, Helping Hands aims to bring people together, fostering a community where everyone looks out for one another.

### Conclusion

Helping Hands is more than just an app; it’s a movement to rekindle the sense of community and solidarity that seems to have faded in our modern world. Join us in making neighborhoods friendlier and more supportive places to live. Together, we can make a difference, one helping hand at a time.

## Technologies Used

**Backend:**
- Gradle
- Java
- Spring Boot
- H2 Database
- Spring Data JPA
- Spring Web MVC
- Spring REST Controller
- REST API

**Frontend:**
- React
- JavaScript/TypeScript
- Axios
- Bootstrap
- CRACO
- React Icons
- CSS
- Geoapify

## Launch & Development

The onboarding process is crucial for a new developer and it involves training to ensure both the backend and frontend components function properly.

### Cloning the Repositories

**Server:**
```sh
git clone https://github.com/sopra-fs24-group-19/server-group19
```

**Client:**
```sh
git clone https://github.com/sopra-fs24-group-19/client-group19
```

### Running the application

**Backend**

To get the backend running, the developer must ensure to have JDK 21 installed and Git is set up. The commands to start the server are:

To run the application:

```sh
./gradlew bootRun
```
To run the tests:

```sh
./gradlew test
```

**Frontend**

For the client, the developer must ensure to have Node.js version 20.11.0 and npm version 10.4.0 installed.

Installing Dependencies:
It is important to ensure all dependencies are installed with:
```sh
npm install
```
To run the application:
```sh
npm run start
```

## How to Use the Application
### Registration and Login
To begin using the application, users must first register by providing the following information: name, username, and password. After registration, users will be redirected to the HomeFeed page. To view all tasks, navigate to the "My Profile" page located at the top left corner and complete the remaining profile details, including address and the maximum radius for viewing tasks from the provided address. Optionally, users can also add their phone number. 

The user's username and remaining coin balance (initially set to 50 coins upon registration) are always displayed in the page header, next to the logout button.


### Creating a Task
After completing the profile, users can either view existing tasks or create a new one. To create a new task, click on the "Create New Task" button located at the top left and enter the following information: task title, description, compensation (in coins), address where the task needs to be performed, date, and an approximate duration of the task. 

Users can verify the successful creation of the task by navigating to the "My Tasks" page, where they can view and manage their posted tasks. This includes checking the list of applicants by clicking on "Check out Helpers" or deleting the task by clicking on "Delete Task."


### Applying to a task
Once a task is created, it will appear in the HomeFeed of users whose filters, set directly on the HomeFeed page and in "My Profile," match the task's criteria.

Users can apply to assist with a task by clicking the "Help" button. The task creator will see the new applicant in real-time under "My Tasks" -> "Check out Helpers." The creator can view the applicant's profile by clicking on "Look at Ratings" and accept them as a helper by clicking "Accept as Helper".

Applicants can withdraw their application by clicking "Withdraw my application" until they are selected as helpers.


### To-Do List
Once a helper is accepted, both the creator and the helper can access the To-Do list, which outlines the actions required to complete the task. The creator can access this list by clicking on "My Tasks" -> "Check out the To-Do List," and the helper can access it by clicking on "My Tasks" -> "Look at your To-Do List".

On the To-Do list page, both the creator and the helper can add tasks related to the ongoing task. The person who posted the To-Do can update or delete it, but only the task creator can mark To-Dos as completed.


### Reviews
Only when all To-Dos are marked as done can users click on "Mark Task As Done," which redirects them to the page for leaving a review for the other user. If the review is not left immediately, it can be done later on the "My Tasks" or "My Applications" page by clicking on "Leave a review."

Users can also view their profiles and respective reviews by clicking on "My Profile" -> "Check out my reviews".


### Leaderboard
The spirit of Helping Hands is to create a supportive community where everyone helps as much as they can. Therefore, we have included a page called "Leaderboard," accessible by clicking on the trophy icon next to "Create New Task." This page allows users to view all members and identify the most virtuous ones who have helped others with the most tasks.
