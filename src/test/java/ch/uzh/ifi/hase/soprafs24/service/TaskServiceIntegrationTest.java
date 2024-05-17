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

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

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

        User testCreator = new User();
        testCreator.setCoinBalance(50);
        testCreator.setName("testName");
        testCreator.setUsername("testUsername");
        testCreator.setPassword("testPassword");
        User createdUser = userService.createUser(testCreator);

        Task testTask = new Task();
        testTask.setTitle("testTitle");
        testTask.setDescription("testDescription");
        testTask.setAddress("testAddress");
        testTask.setCreator(testCreator);
        testTask.setDate(new Date());
        testTask.setDuration(30);
        testTask.setPrice(20);

        Task createdTask = taskService.createTask(testTask, testCreator.getId());

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

    @Test
    public void getTasks_ReturnsTasksOnOrAfterToday() {

        Date now = new Date();
        List<Task> tasks = taskService.getTasks();

        assertTrue(tasks.stream().allMatch(task -> !task.getDate().before(now)));
    }

    @Test
    public void getTaskById_ValidId_ReturnsTask() {

        Task foundTask = taskService.getTaskById(task.getId());

        assertEquals(task.getId(), foundTask.getId());
    }

    @Test
    public void getTaskById_InvalidId_ThrowsEntityNotFoundException() {

        long invalidTaskId = -1;

        assertThrows(EntityNotFoundException.class, () -> taskService.getTaskById(invalidTaskId));
    }

    @Test
    public void getTasksByCreator_ValidUserId_ReturnsTasks() {

        List<Task> tasks = taskService.getTasksByCreator(creator.getId());

        assertTrue(tasks.stream().allMatch(task -> task.getCreator().getId().equals(creator.getId())));
    }

    @Test
    public void getCandidatesForTaskWithId_ValidTaskId_ReturnsCandidates() {

        List<User> candidates = taskService.getCandidatesForTaskWithId(task.getId());

        assertEquals(candidates.size(), task.getCandidates().size());
    }





}
