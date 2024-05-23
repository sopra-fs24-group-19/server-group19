# Helping Hands - Reinventing Community Solidarity

## Introduction
In an age where technology dominates our lives, the sense of community in our neighborhoods has sadly diminished. It’s not uncommon today for people to not know the names of their neighbors, with greetings reduced to mere formalities. The Helping Hands project is born out of the necessity to revive that lost sense of community, reminiscent of the times of our parents and grandparents.
### About the App

Helping Hands is designed to strengthen community bonds by enabling users to request and offer help within their local neighborhoods. At the heart of our platform is the belief that communities thrive when neighbors support each other. Users can define their search radius to find or offer help nearby, making assistance both timely and relevant to local needs. To facilitate a fair exchange of services, our app uses a virtual coin system. Users earn coins by helping others, which they can then use to request help for themselves. This coin-based economy not only incentivizes participation but also ensures that the exchange of services remains balanced. Here’s how it works:

1. **Registration**: Users can sign up and create a profile.
2. **Posting Tasks**: Users can post requests for help with specific tasks, such as grocery shopping, pet sitting, or any other daily activities.
3. **Specifying a radius**: Users can specify their search radius in order to make sure they see only tasks that are nearby.
4. **Finding Tasks**: By entering their home address, users can view tasks nearby. This ensures that help is always close at hand.
5. **Filtering Tasks**: Users can specify their search criteria in order to make sure they see only the tasks that align with their needs.
6. **Applying for tasks**: Users can select tasks that match their skills and preferences and offer their help.
7. **Selecting a helper**: A user that has posted a task can select the most suited candidate from a list of users that have applied.
8. **Shared to-do list**: In order to better organize the work, while a task is ongoing, both parties have access to a real-time shared to-do list section.
9. **Earning Coins**: Upon completing tasks, users earn symbolic coins. These coins incentivize helping others and can be used to post their own tasks.
10. **Community Feedback**: A review system ensures that the individuals offering help are trustworthy and competent.
11. **Leaderboard**: There is a leaderboard where users can see the most helpful people in the community, fostering a spirit of friendly competition and recognition.

## Backend Technologies

- **[Gradle](https://gradle.org/)**: Gradle is a powerful build automation tool used primarily for Java projects. It simplifies the build process by handling everything from dependency management to packaging the applications.

- **[Java](https://www.java.com/)**: Java is a robust, object-oriented programming language that ensures portability and high performance across platforms, making it ideal for developing backend services.

- **[Spring Boot](https://spring.io/projects/spring-boot)**: Spring Boot makes it easy to create stand-alone, production-grade Spring based applications that you can "just run". It simplifies the server side of development by accelerating setup and deployment.

- **[H2 Database](https://www.h2database.com/)**: H2 is a lightweight, in-memory database known for its fast performance and simplicity in integration, often used for development and testing.

- **[Spring Data JPA](https://spring.io/projects/spring-data-jpa)**: This module of the larger Spring framework simplifies data access operations in relational databases by abstracting boilerplate CRUD operations, enhancing the ease of database interactions.

- **[Spring Web MVC](https://spring.io/guides/gs/serving-web-content/)**: A framework that follows the Model-ViewController architecture, Spring MVC makes it easier to build flexible and loosely coupled web applications.

- **[Spring REST Controller](https://spring.io/guides/gs/rest-service/)**: This is part of the Spring MVC framework which facilitates the creation of RESTful web services. It allows the building of scalable and secure web APIs.

- **[REST API](https://restfulapi.net/)**: REST APIs allow different software applications to communicate over HTTP, using a set of defined rules for operations such as GET, POST, PUT, and DELETE, which correspond to read, create, update, and delete actions.


## High-level Components

The primary components of the backend are the entities, service, controller and repository classes. For each of the entities (Rating, Task, User, Todo) there are corresponding controllers, services and repositories. Additionally, we built a mapper class that maps external DTOs to internal entities. In the following, we illustrate this architecture using the task entity as an example.

### Entities

Entities are the fundamental components of the system, acting as data models that map directly to database tables through ORM (Object-Relational Mapping) frameworks like Spring Data JPA. Each entity encapsulates the data and the state of the application. For example, a Task entity represents tasks in a todo application and typically includes fields like id, title, description, and status. These entities are annotated with JPA annotations to define the relationships with other tables and database constraints:

```sh
@Entity
@Table(name = "TASK")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = true)
    private String latitude;

    @Column(nullable = true)
    private String longitude;

    @Enumerated
    @Column(nullable=false)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "creatorId", referencedColumnName = "id")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "helperId", referencedColumnName = "id")
    private User helper;

    @ManyToMany(mappedBy = "applications")
    private List<User> candidates = new ArrayList<>();

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Todo> todos;

    @OneToMany(mappedBy = "task")
    private List<Rating> ratings;
```

For further reference, have a look at the [task entity](https://github.com/sopra-fs24-group-19/server-group19/blob/2f2ca978a614c3751310f6c04d6e92a8c3622b34/src/main/java/ch/uzh/ifi/hase/soprafs24/entity/Task.java).


### Controllers

The controllers are responsible for processing requests sent by the frontend. They serve as an intermediary between the client-side applications and the backend services. Using the Spring REST Controller framework, controllers manage all requests according to the HTTP standard methods (GET, POST, PUT, DELETE). Controllers map incoming HTTP requests to the appropriate service layer methods using annotations like `@GetMapping`, `@PostMapping`, `@PutMapping`, and `@DeleteMapping`. They handle the conversion of Data Transfer Objects (DTOs) to internal entities and vice versa. This ensures that only necessary data is exposed to the client and helps maintain a clean separation between the client-side models and the server-side models. Additionally, controllers manage all necessary parameters, including tokens for authentication and path parameters to identify the IDs of the instances in question, ensuring secure and accurate processing of client requests.

This is an example of a PostMapping within the task controller: 

```sh
    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void createTask(@RequestBody TaskPostDTO taskPostDTO) {
        Task taskInput = DTOMapper.INSTANCE.convertTaskPostDTOToEntity(taskPostDTO);
        long creatorId = taskPostDTO.getCreatorId();
        Task createdTask = taskService.createTask(taskInput,creatorId);
    }
```
For further reference, have a look at the [task controller class](https://github.com/sopra-fs24-group-19/server-group19/blob/6f3f2adee63458f4442fca629a66abc2f94ea5e8/src/main/java/ch/uzh/ifi/hase/soprafs24/controller/TaskController.java).

### Services

The services offer methods for all kinds of requests related to their corresponding entities. They implement most of the business logic, ensuring that the application functions correctly and efficiently. Services interact with repositories through interfaces, adhering to the principles of dependency injection and inversion of control, which makes the system more modular and testable.

Here’s an example of a service function:

```sh
public Task createTask(Task newTask, long userId) {
    User creator = userService.getUserById(userId);
    boolean valid = checkIfCreatorHasEnoughTokens(creator, newTask);
    if (!valid) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Creator does not have enough credits");
    }
    newTask.setCreator(creator);
    newTask.setStatus(TaskStatus.CREATED);
    userService.subtractCoins(creator, newTask.getPrice());
    newTask = taskRepository.save(newTask);
    taskRepository.flush();
    return newTask;
}
```

We paid particular attention to managing dependencies within the various classes. While we minimized cross-calling between different services to maintain separation of concerns, the services do communicate with each other where business requirements dictate. This careful design allows for a balance between modularity and necessary inter-service communication.

For further reference, dive into the [task service class](https://github.com/sopra-fs24-group-19/server-group19/blob/2f2ca978a614c3751310f6c04d6e92a8c3622b34/src/main/java/ch/uzh/ifi/hase/soprafs24/service/TaskService.java).

### Repositories

The repositories serve as the interface between the services and the database. For each table in the database corresponding to the entities, there is a separate repository. Repositories handle all database interactions, which facilitates testing and ensures a clean separation of concerns by avoiding cross-dependencies. This design pattern follows the principles of the Repository Pattern, promoting a more modular and maintainable codebase.

Repositories extend Spring Data JPA’s `JpaRepository` interface, providing a range of ready-to-use methods for common database operations like saving, deleting, and finding entities. Additionally, custom query methods can be defined based on the naming conventions supported by Spring Data JPA.

For instance, here is an example of a repository interface for the `Todo` entity:



```sh
@Repository("todoRepository")
public interface TodoRepository extends JpaRepository<Todo, Long> {

    Todo findTodoById(Long id);

    List<Todo> findByTaskId(Long taskId);

    List<Todo> findAllByTaskId(Long taskId);
}
```

By isolating database access to repository classes, the architecture promotes a cleaner and more testable codebase, ensuring that data access logic is centralized and easily manageable.

For further reference, have a look at the [task repository](https://github.com/sopra-fs24-group-19/server-group19/blob/2f2ca978a614c3751310f6c04d6e92a8c3622b34/src/main/java/ch/uzh/ifi/hase/soprafs24/repository/TaskRepository.java).


## Launch & Development

The onboarding process is crucial for a new developer and it involves training to ensure both the backend and frontend components function properly.

### Cloning the Repository

```sh
git clone https://github.com/sopra-fs24-group-19/server-group19
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
npm start
```

## Roadmap

Envisioning continued development on the proposed application, it would be interesting to implement the following functionalities both on the frontend and the supporting backend services:

- **Notification System**: Implement a notification system that can communicate with users via various platforms such as email, SMS, or a dedicated application released on Android and iOS. This system would notify users about service updates, such as being selected for a task, the completion of a to-do, or receiving a review.

- **Real-time Interaction**: Implementing websockets for better handling of real time interaction.

- **Enhanced Task Filtering**: Incorporate additional filtering systems to improve task search capabilities. As the number of tasks grows with the increase in users, effective filtering becomes essential. Implementing a search bar that allows filtering by keywords could fulfill this requirement, making it easier for users to find relevant tasks.

### Conclusion

Helping Hands is more than just an app; it’s a movement to rekindle the sense of community and solidarity that seems to have faded in our modern world. Join us in making neighborhoods friendlier and more supportive places to live. Together, we can make a difference, one helping hand at a time.

## Authors and Acknowledgement

### Authors

- **Dana Rapp**  
  - Matriculation Number: 23731995  
  - Email: [dana.rapp@uzh.ch](mailto:dana.rapp@uzh.ch)  
  - GitHub: [dana-jpg](https://github.com/dana-jpg)

- **Francesco Manzionna**  
  - Matriculation Number: 23745979  
  - Email: [francesco.manzionna@uzh.ch](mailto:francesco.manzionna@uzh.ch)  
  - GitHub: [Holwy](https://github.com/Holwy)

## License

This project is licensed under the GNU General Public License v3.0, see the [LICENSE](LICENSE) file for details.

We chose the GPL v3.0 to ensure that our project remains free and open, allowing users to use, modify, and distribute the software while ensuring that all modifications remain open and accessible to the community.

