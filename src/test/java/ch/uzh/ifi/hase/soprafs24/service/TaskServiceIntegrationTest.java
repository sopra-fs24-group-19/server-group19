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

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

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

    private User creator, helper, unauthorizedUser;
    private Task task;


    @BeforeEach
    public void setup() {
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
        task.setDescription("The kitchen sink is broken.");
        task.setAddress("1234 Street");
        task.setCreator(creator);
        task.setHelper(helper);
        task.setDate(new Date());
        task.setDuration(60);
        task.setPrice(20);
        task.setStatus(TaskStatus.CREATED);
        task = taskRepository.save(task);
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

        User testCreator = new User();
        //testCreator.setCoinBalance(20);
        testCreator.setId(1L);
        testCreator.setName("testName");
        testCreator.setUsername("testUsername");
        testCreator.setPassword("testPassword");
        User createdUser = userService.createUser(testCreator);

        Task testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("testTitle");
        testTask.setDescription("testDescription");
        testTask.setAddress("testAddress");
        testTask.setCreator(testCreator);
        testTask.setDate(new Date());
        testTask.setDuration(30);
        testTask.setPrice(60);
        //Task createdTask = taskService.createTask(testTask, createdUser.getId());

        assertThrows(ResponseStatusException.class, () -> taskService.createTask(testTask, createdUser.getId()));
    }

    @Test
    public void confirmTask_Success_ByCreator() {
        String token = creator.getToken();
        Task confirmedTask = taskService.confirmTask(task.getId(), token);

        assertEquals(TaskStatus.CONFIRMED_BY_CREATOR, confirmedTask.getStatus());
    }


    @Test
    public void confirmTask_Success_ByHelper() {
        String token = helper.getToken();
        Task originalTask = taskRepository.findById(task.getId())
                .orElseThrow(() -> new NoSuchElementException("Task not found"));

        originalTask.setStatus(TaskStatus.CONFIRMED_BY_CREATOR);

        taskRepository.save(originalTask);
        Task confirmedTask = taskService.confirmTask(task.getId(), token);
        helper = userRepository.findById(helper.getId()).orElseThrow();
        assertEquals(TaskStatus.DONE, confirmedTask.getStatus());
        assertEquals(70, helper.getCoinBalance());
    }


    @Test
    public void confirmTask_Unauthorized_User() {

        String token = unauthorizedUser.getToken();

        assertThrows(ResponseStatusException.class, () -> taskService.confirmTask(task.getId(), token));
    }

    @Test
    public void deleteTaskWithId_Success() {
        String token = creator.getToken();
        int initialCoinBalance = creator.getCoinBalance();

        taskService.deleteTaskWithId(task.getId(), token);
        creator = userRepository.findById(creator.getId()).orElseThrow();

        assertFalse(taskRepository.findById(task.getId()).isPresent());
        assertEquals(initialCoinBalance + task.getPrice(), creator.getCoinBalance());
    }

    @Test
    public void deleteTaskWithId_UnauthorizedUser_ThrowsException() {
        String token = unauthorizedUser.getToken();

        assertThrows(ResponseStatusException.class, () -> taskService.deleteTaskWithId(task.getId(), token));
    }

    @Test
    public void deleteTaskWithId_NonExistentTask_ThrowsException() {
        long nonExistentTaskId = -1;
        String token = creator.getToken();

        assertThrows(NoSuchElementException.class, () -> taskService.deleteTaskWithId(nonExistentTaskId, token));
    }

}
