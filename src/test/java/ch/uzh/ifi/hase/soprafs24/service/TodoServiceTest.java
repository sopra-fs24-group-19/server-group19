package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Task;
import ch.uzh.ifi.hase.soprafs24.entity.Todo;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.TodoRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.TodoGetDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;
    @Mock
    private TaskService taskService;
    @Mock
    private UserService userService;
    @InjectMocks
    private TodoService todoService;

    private User user, invalidUser, helper;
    private Task task;
    private Todo todo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setToken("validToken");

        invalidUser = new User();
        invalidUser.setId(2L);
        invalidUser.setToken("invalidToken");

        helper = new User();
        helper.setId(3L);
        helper.setToken("validToken");

        task = new Task();
        task.setId(1L);
        task.setCreator(user);
        task.setHelper(helper);

        todo = new Todo();
        todo.setId(1L);
        todo.setTask(task);
        todo.setAuthor(user);
        todo.setDescription("description");
        todo.setDone(false);
    }

    @Test
    public void createTodo_success() {
        when(taskService.getTaskById(anyLong())).thenReturn(task);
        when(userService.getUserIdByToken(user.getToken())).thenReturn(user.getId());
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        todoService.createTodo(todo, task.getId(), user.getToken());

        verify(todoRepository).save(todo);
    }

    @Test
    public void createTodo_unauthorizedUser_throwsException() {
        when(taskService.getTaskById(anyLong())).thenReturn(task);
        when(userService.getUserIdByToken("invalidToken")).thenReturn(invalidUser.getId());

        assertThrows(ResponseStatusException.class, () -> todoService.createTodo(todo, task.getId(), "invalidToken"),
                "Should throw an exception if the user is neither creator nor helper");
    }

    @Test
    public void updateTodo_success() {
        when(todoRepository.findById(todo.getId())).thenReturn(java.util.Optional.of(todo));
        when(userService.getUserIdByToken(user.getToken())).thenReturn(user.getId());

        todo.setDescription("Updated description");
        todo.setDone(true);
        todoService.updateTodo(todo, user.getToken(), todo.getId());

        verify(todoRepository).saveAndFlush(todo);
    }

    @Test
    public void updateDescription_unauthorizedUpdate_throwsException() {
        when(todoRepository.findById(todo.getId())).thenReturn(java.util.Optional.of(todo));
        when(userService.getUserIdByToken("invalidToken")).thenReturn(invalidUser.getId());

        todo.setDescription("new description");

        assertThrows(ResponseStatusException.class, () -> todoService.updateTodo(todo, "invalidToken", todo.getId()),
                "Should throw an exception if the user is not the author of the todo");
    }

    @Test
    public void areAllTodosDone_allDone() {
        List<Todo> todos = List.of(new Todo() {{ setDone(true); }}, new Todo() {{ setDone(true); }});
        when(todoRepository.findAllByTaskId(task.getId())).thenReturn(todos);

        assertTrue(todoService.areAllTodosDone(task.getId()), "Should return true when all todos are done");
    }

    @Test
    public void areAllTodosDone_notAllDone() {
        List<Todo> todos = List.of(new Todo() {{ setDone(true); }}, new Todo() {{ setDone(false); }});
        when(todoRepository.findAllByTaskId(task.getId())).thenReturn(todos);

        assertFalse(todoService.areAllTodosDone(task.getId()), "Should return true when all todos are done");
    }

    @Test
    public void getTodosFromTask_success() {
        when(todoRepository.findByTaskId(task.getId())).thenReturn(Arrays.asList(todo));
        when(userService.getUserIdByToken(user.getToken())).thenReturn(user.getId());

        List<TodoGetDTO> result = todoService.getTodosFromTask(user.getToken(), task.getId());

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(todo.getId(), result.get(0).getId());
        assertEquals(todo.getDescription(), result.get(0).getDescription());
    }

    @Test
    public void deleteTodo_success() {
        when(todoRepository.findTodoById(todo.getId())).thenReturn(todo);
        when(userService.getUserByToken(user.getToken())).thenReturn(user);

        assertDoesNotThrow(() -> todoService.deleteTodo(todo.getId(), user.getToken()));

        verify(todoRepository).delete(todo);
    }

    @Test
    public void deleteTodo_unauthorizedUser_throwsException() {
        when(todoRepository.findTodoById(todo.getId())).thenReturn(todo);
        when(userService.getUserByToken(invalidUser.getToken())).thenReturn(invalidUser);

        assertThrows(ResponseStatusException.class,
                () -> todoService.deleteTodo(todo.getId(), invalidUser.getToken()),
                "Should throw an exception if the user is not the creator of the todo");
    }

    @Test
    public void tokenValidationTodo_unauthorizedUser_throwsException() {
        when(todoRepository.findTodoById(todo.getId())).thenReturn(todo);
        when(userService.getUserByToken(invalidUser.getToken())).thenReturn(invalidUser);

        assertThrows(ResponseStatusException.class,
                () -> todoService.tokenValidationTodo(todo, invalidUser.getToken()),
                "Should throw an exception if the user is not the creator of the todo");
    }

    @Test
    public void updateTodo_unauthorizedIsDoneUpdate_throwsException() {
        when(todoRepository.findById(todo.getId())).thenReturn(java.util.Optional.of(todo));
        when(userService.getUserIdByToken(invalidUser.getToken())).thenReturn(invalidUser.getId());

        todo.setDone(true);

        assertThrows(ResponseStatusException.class,
                () -> todoService.updateTodo(todo, invalidUser.getToken(), todo.getId()),
                "Should throw an exception if the user is not the creator or helper of the task when updating isDone");
    }

    @Test
    public void updateTodo_unauthorizedDescriptionUpdate_throwsException() {
        when(todoRepository.findById(todo.getId())).thenReturn(java.util.Optional.of(todo));
        when(userService.getUserIdByToken(invalidUser.getToken())).thenReturn(invalidUser.getId());

        todo.setDescription("Updated description");

        assertThrows(ResponseStatusException.class,
                () -> todoService.updateTodo(todo, invalidUser.getToken(), todo.getId()),
                "Should throw an exception if the user is not the author of the todo when updating description");
    }

}
