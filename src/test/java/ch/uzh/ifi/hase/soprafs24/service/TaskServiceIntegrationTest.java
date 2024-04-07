package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.TaskStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Task;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.TaskRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
public class TaskServiceIntegrationTest {

    @Qualifier("taskRepository")
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void createTask_validInputs_success() {
        // given
        User testCreator = new User();
        testCreator.setCoinBalance(50);
        testCreator.setName("testName");
        testCreator.setUsername("testUsername");
        testCreator.setPassword("testPassword");
        //comment out after having made sure the id is correctly set!
        //testCreator.setId(1L);
        User createdUser = userService.createUser(testCreator);

        Task testTask = new Task();
        //testTask.setId(1L);
        testTask.setTitle("testTitle");
        testTask.setDescription("testDescription");
        testTask.setAddress("testAddress");
        testTask.setCreator(testCreator);
        testTask.setDate(new Date());
        testTask.setDuration(30);
        testTask.setPrice(20);

        // when
        //User createdUser = userService.createUser(testCreator);
        Task createdTask = taskService.createTask(testTask, testCreator.getId());

        // then
        assertEquals(testTask.getId(), createdTask.getId());
        assertEquals(testCreator.getId(), createdUser.getId());
        assertEquals(testTask.getDescription(), createdTask.getDescription());
        assertEquals(testTask.getTitle(), createdTask.getTitle());
        assertEquals(testTask.getPrice(), createdTask.getPrice());
        assertEquals(testTask.getDate(), createdTask.getDate());
        assertEquals(testTask.getDuration(), createdTask.getDuration());
        assertEquals(testTask.getAddress(), createdTask.getAddress());
    }

    @Test
    public void createTask_lowCoinBalance_throwsException() {
        //assertNull(userRepository.findByUsername("testUsername"));

        User testCreator = new User();
        testCreator.setCoinBalance(20);
        testCreator.setName("testName");
        testCreator.setUsername("testUsername");
        testCreator.setPassword("testPassword");
        User createdUser = userService.createUser(testCreator);

        Task testTask = new Task();
        //testTask.setId(1L);
        testTask.setTitle("testTitle");
        testTask.setDescription("testDescription");
        testTask.setAddress("testAddress");
        testTask.setCreator(testCreator);
        testTask.setDate(new Date());
        testTask.setDuration(30);
        testTask.setPrice(50);

        assertThrows(ResponseStatusException.class, () -> taskService.createTask(testTask, testCreator.getId()));
    }
}
