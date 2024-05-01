package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Todo;
import ch.uzh.ifi.hase.soprafs24.repository.*;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Task;
import ch.uzh.ifi.hase.soprafs24.rest.dto.TodoGetDTO;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@Transactional
public class TodoService {
    private final Logger log = LoggerFactory.getLogger(TodoService.class);
    private final TaskService taskService;
    private final UserService userService;
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    @Autowired
    public TodoService(
            @Qualifier("todoRepository") TodoRepository todoRepository, TaskService taskService,
            UserService userService, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.taskService = taskService;
        this.userService = userService;
        this.userRepository= userRepository;
    }

    public void createTodo(Todo todo, long taskId, String token) {
        // from here
        Task task = taskService.getTaskById(taskId);
        User taskCreator = task.getCreator();
        User taskHelper = task.getHelper();
        long authorId = userService.getUserIdByToken(token);

        if (authorId != taskCreator.getId() && authorId != taskHelper.getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Only the creator or helper of this task are authorized to create a to-do.");
        }

        todo.setTask(task);
        todo.setDone(false);
        todo.setAuthor(userService.getUserById(authorId));
        todoRepository.save(todo);
        todoRepository.flush();
    }

    //QUESTION: Only the creator of a task and the helper are able to see all todos?
    public List<TodoGetDTO> getTodosFromTask( String token, long taskId){
        //Tokenvalidation

        List<Todo> todoEntityList = todoRepository.findByTaskId(taskId);
        List<TodoGetDTO> todoGetDtoList = new ArrayList<>();

        for (Todo todo : todoEntityList) {
            TodoGetDTO todoGetItem = DTOMapper.INSTANCE.convertEntityToTodoGetDTO(todo);
            User author = todo.getAuthor();
            long authorId = author.getId();
            todoGetItem.setAuthorId(authorId);
            todoGetDtoList.add(todoGetItem);
        }
        return todoGetDtoList;}


    public void deleteTodo(Todo todoToDelete, String token) {
        Todo todoRetrieved = todoRepository.findTodoById(todoToDelete.getId());
        tokenValidationTodo(todoRetrieved, token);

        todoRepository.delete(todoRetrieved);
    }
    // Function to be inserted in Dana's createTodo
    private boolean tokenValidation(Todo todo, String token) {
        Task task = todo.getTask();
        User taskCreator = task.getCreator();
        User taskHelper = task.getHelper();
        long authenticatedUser = userService.getUserIdByToken(token);

        if (authenticatedUser != taskCreator.getId() && authenticatedUser != taskHelper.getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Only the creator or helper of this task are authorized to edit the todo list.");
        }
        return true;
    }
    //Checks the author of a todo and the user who wants to delete it matches
    private boolean tokenValidationTodo(Todo todo, String token) {
        Task task = todo.getTask();
        User todoAuthor = todo.getAuthor();
        User authenticatedUser = this.userRepository.findUserByToken(token);

        if (authenticatedUser != todoAuthor) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Only the creator of a Todo is authorized to delete it.");
        }
        return true;
    }

    public void updateTodo(Todo todoInput, String token, long id) {
        Todo existingTodo = todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found with id: " + id));

        long userId = userService.getUserIdByToken(token);

        if (!existingTodo.getDescription().equals(todoInput.getDescription())) {
            if (userId != existingTodo.getAuthor().getId()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Only the author of this Todo is authorized to update the description.");
            }
            existingTodo.setDescription(todoInput.getDescription());
        }

        if (existingTodo.isDone() != todoInput.isDone()) {
            if (userId != existingTodo.getTask().getCreator().getId()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Only the creator of the task can update the status of this Todo.");
            }
            existingTodo.setDone(todoInput.isDone());
        }
        todoRepository.saveAndFlush(existingTodo);
    }

}
