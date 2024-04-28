package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Todo;
import ch.uzh.ifi.hase.soprafs24.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import ch.uzh.ifi.hase.soprafs24.repository.ApplicationsRepository;
import ch.uzh.ifi.hase.soprafs24.repository.TaskRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.TaskPutDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Application;
import ch.uzh.ifi.hase.soprafs24.entity.Task;
//import ch.uzh.ifi.hase.soprafs24.service.UserService;
import ch.uzh.ifi.hase.soprafs24.constant.TaskStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.NoSuchElementException;
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

    public void createTodo(Todo todo, long taskId, String token){
        Task task = taskService.getTaskById(taskId);
        User taskCreator = task.getCreator();
        User taskHelper = task.getHelper();
        long authorId = userService.getUserIdByToken(token);

        if (authorId != taskCreator.getId() && authorId != taskHelper.getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Only the creator or helper of this task are authorized to confirm it.");
        }
        todo.setTask(task);
        todo.setDone(false);
        todo.setAuthor(userService.getUserById(authorId));
        todoRepository.save(todo);
        todoRepository.flush();
    }

    public void updateTodo(Todo todoInput, String token, long id) {

        Todo existingTodo = todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found with id: " + todoInput.getId()));

        long userId = userService.getUserIdByToken(token);
        if (userId != existingTodo.getAuthor().getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Only the author of this Todo is authorized to update it.");
        }

        existingTodo.setDescription(todoInput.getDescription());
        existingTodo.setDone(todoInput.isDone());

        todoRepository.save(existingTodo);
    }
}
