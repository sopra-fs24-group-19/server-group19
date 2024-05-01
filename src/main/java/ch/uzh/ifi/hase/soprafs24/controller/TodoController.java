package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.TodoService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TodoController {

    private final TodoService todoService;

    TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping("/todo")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void createTodo(@RequestBody TodoPostDTO todoPostDTO, @RequestHeader("Authorization") String token) {
        Todo todoInput = DTOMapper.INSTANCE.convertTodoPostDTOToEntity(todoPostDTO);
        long taskId = todoPostDTO.getTaskId();

        todoService.createTodo(todoInput, taskId, token);
    }
    //TODO Refactor this method
    @DeleteMapping("/todo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteTodo(@RequestBody TodoPutDTO todoPutDTO, @RequestHeader("Authorization") String token) {
        Todo todoToDelete = DTOMapper.INSTANCE.convertTodoPutDTOToEntity(todoPutDTO);
        todoService.deleteTodo(todoToDelete, token);
    }

    @PutMapping("/todo/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void updateTodo(@RequestBody TodoPutDTO todoPutDTO, @RequestHeader("Authorization") String token,
            @PathVariable("id") long id) {
        Todo todoInput = DTOMapper.INSTANCE.convertTodoPutDTOToEntity(todoPutDTO);
        // long taskId = todoPostDTO.getTaskId();
        todoService.updateTodo(todoInput, token, id);
    }

    @GetMapping("/todo/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<TodoGetDTO> getTodosFromTask(@RequestHeader("Authorization") String token, @PathVariable("id") long taskId) {
        List<TodoGetDTO> todosList= todoService.getTodosFromTask(token, taskId);
        return todosList;
    }

    @GetMapping("/allTodosDone/{taskId}")
    public ResponseEntity<Boolean> areAllTodosDone(@PathVariable Long taskId) {
        boolean allDone = todoService.areAllTodosDone(taskId);
        return ResponseEntity.ok(allDone);
    }
}
