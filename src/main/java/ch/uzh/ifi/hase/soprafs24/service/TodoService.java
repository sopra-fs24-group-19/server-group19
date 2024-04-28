package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Todo;
import ch.uzh.ifi.hase.soprafs24.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Task;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@Transactional
public class TodoService {
    private final Logger log = LoggerFactory.getLogger(TodoService.class);
    private final TaskService taskService;
    private final UserService userService;
    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(
            @Qualifier("todoRepository") TodoRepository todoRepository, TaskService taskService,
            UserService userService) {
        this.todoRepository = todoRepository;
        this.taskService = taskService;
        this.userService = userService;
    }

    public void createTodo(Todo todo, long taskId, String token) {
        // from here
        Task task = taskService.getTaskById(taskId);
        User taskCreator = task.getCreator();
        User taskHelper = task.getHelper();
        long authorId = userService.getUserIdByToken(token);

        if (authorId != taskCreator.getId() && authorId != taskHelper.getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Only the creator or helper of this task are authorized to confirm it.");
        }
        // to here should be in a separate function
        todo.setTask(task);
        //shouldn't the description be updated
        todo.setDone(false);
        todo.setAuthor(userService.getUserById(authorId));
        todoRepository.save(todo);
        todoRepository.flush();
    }


    public void deleteTodo(Todo todoToDelete, String token) {
        Todo todoRetrieved = todoRepository.findTodoById(todoToDelete.getId());
        tokenValidation(todoRetrieved, token);

        todoRepository.delete(todoRetrieved);
    }

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
}