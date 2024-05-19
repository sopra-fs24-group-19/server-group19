package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.TaskStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Task;
import ch.uzh.ifi.hase.soprafs24.entity.Todo;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.TaskRepository;
import ch.uzh.ifi.hase.soprafs24.repository.TodoRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.TodoGetDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
public class TodoServiceIntegrationTest {

    @Qualifier("todoRepository")
    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TodoService todoService;

    private User creator, helper, unauthorizedUser;
    private Task task;
    private Todo todoHelper, todoCreator;

    @BeforeEach
    public void setup() {
        todoRepository.deleteAll();
        taskRepository.deleteAll();
        userRepository.deleteAll();

        creator = new User();
        creator.setCoinBalance(100);
        creator.setName("creatorName");
        creator.setUsername("creator");
        creator.setPassword("password1");
        creator.setToken("creatorToken");
        creator = userRepository.save(creator);

        // Setup helper
        helper = new User();
        helper.setCoinBalance(50);
        helper.setName("helperName");
        helper.setUsername("helper");
        helper.setPassword("password2");
        helper.setToken("helperToken");
        helper = userRepository.save(helper);

        unauthorizedUser = new User();
        unauthorizedUser.setUsername("intruder");
        unauthorizedUser.setCoinBalance(50);
        unauthorizedUser.setName("intruderName");
        unauthorizedUser.setToken("unauthorizedUserToken");
        unauthorizedUser.setPassword("password3");
        unauthorizedUser = userRepository.save(unauthorizedUser);

        // Setup task
        task = new Task();
        task.setTitle("Fix Sink");
        task.setId(1L);
        task.setDescription("The kitchen sink is broken.");
        task.setAddress("1234 Street");
        task.setCreator(creator);
        task.setHelper(helper);
        task.setDate(new Date());
        task.setDuration(60);
        task.setPrice(20);
        task.setStatus(TaskStatus.CREATED);
        task = taskRepository.save(task);

        todoHelper = new Todo();
        todoHelper.setDescription("todo1");
        todoHelper.setDone(false);
        todoHelper.setId(6L);
        todoHelper.setAuthor(helper);
        todoHelper.setTask(task);
        todoHelper = todoRepository.save(todoHelper);

        todoCreator = new Todo();
        todoCreator.setDescription("todo2");
        todoCreator.setId(5L);
        todoCreator.setDone(false);
        todoCreator.setAuthor(creator);
        todoCreator.setTask(task);
        todoCreator = todoRepository.save(todoCreator);
    }

    @Test
    public void testCreateTodo_Success() {

        Todo newTodo = new Todo();
        newTodo.setDescription("Check water connection");
        newTodo.setDone(false);

        long taskId = task.getId();
        String token = creator.getToken();

        todoService.createTodo(newTodo, taskId, token);
        Todo savedTodo = todoRepository.findById(newTodo.getId()).get();
        assertNotNull(savedTodo);
        assertEquals("Check water connection", savedTodo.getDescription());
        assertFalse(savedTodo.isDone());
        assertEquals(creator.getId(), savedTodo.getAuthor().getId());
    }

    @Test
    public void testCreateTodo_Unauthorized() {

        Todo newTodo = new Todo();
        newTodo.setDescription("Unauthorized task attempt");
        newTodo.setDone(false);

        String token = unauthorizedUser.getToken();

        assertThrows(ResponseStatusException.class, () -> {
            todoService.createTodo(newTodo, task.getId(), token);
        });
    }

    @Test
    public void testGetTodosFromTask_Success() {

        String token = creator.getToken();

        List<TodoGetDTO> todos = todoService.getTodosFromTask(token, task.getId());

        assertFalse(todos.isEmpty());
        assertEquals(2, todos.size());
    }

    @Test
    public void testDeleteTodo_Success() {

        todoService.deleteTodo(todoCreator.getId(), creator.getToken());
        assertEquals(1, todoRepository.findAll().size());
    }

    @Test
    public void testUpdateTodo_Success() {

        Todo updatedTodo = new Todo();
        updatedTodo.setDescription("Updated description");
        updatedTodo.setDone(true);

        todoService.updateTodo(updatedTodo, creator.getToken(), todoCreator.getId());

        Todo result = todoRepository.findById(todoCreator.getId()).get();
        assertEquals("Updated description", result.getDescription());
        assertTrue(result.isDone());
    }

    @Test
    public void testUpdateTodo_UnauthorizedUserCannotUpdateDescription() {

        Todo existingTodo = todoRepository.findById(todoHelper.getId()).get();
        existingTodo.setDescription("Initial description");

        String anotherUserToken = "someTokenForAnotherUser";

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            todoService.updateTodo(existingTodo, anotherUserToken, existingTodo.getId());
        });

    }

}
