package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.TaskStatus;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.TaskRepository;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskService taskService;

    private Task testTask;
    private User testCreator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("testTitle");
        testTask.setDescription("testDescription");
        testTask.setAddress("testAddress");
        testTask.setCreator(testCreator);
        testTask.setDate(new Date());
        testTask.setDuration(30);
        testTask.setPrice(20);

        testCreator = new User();
        testCreator.setCoinBalance(50);
        testCreator.setId(1L);

        Mockito.when(taskRepository.save(Mockito.any())).thenReturn(testTask);
        Mockito.when(userService.getUserById(Mockito.anyLong())).thenReturn(testCreator);

    }

    @Test
    public void createTask_validInputs_success() {

        Task createdTask = taskService.createTask(testTask, testCreator.getId());

        Mockito.verify(taskRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testTask.getId(), createdTask.getId());
        assertEquals(testCreator.getId(), testTask.getCreator().getId());
        assertEquals(testTask.getDescription(), createdTask.getDescription());
        assertEquals(testTask.getTitle(), createdTask.getTitle());
        assertEquals(testTask.getPrice(), createdTask.getPrice());
        assertEquals(testTask.getDate(), createdTask.getDate());
        assertEquals(testTask.getDuration(), createdTask.getDuration());
        assertEquals(testTask.getAddress(), createdTask.getAddress());

    }

    @Test
    public void createUser_lowCreditBalance_throwsException() {

        testCreator.setCoinBalance(10);

        assertThrows(ResponseStatusException.class, () -> taskService.createTask(testTask, testCreator.getId()));
    }


}
