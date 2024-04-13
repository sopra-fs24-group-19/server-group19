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

import java.util.*;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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

    private User testCandidate;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testCreator = new User();
        testCreator.setCoinBalance(50);
        testCreator.setId(1L);
        testCreator.setToken("validToken");

        testCandidate = new User();
        testCandidate.setCoinBalance(50);
        testCandidate.setId(2L);
        testCandidate.setName("candidateName");
        testCandidate.setUsername("candidateUsername");

        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("testTitle");
        testTask.setDescription("testDescription");
        testTask.setAddress("testAddress");
        testTask.setCreator(testCreator);
        testTask.setDate(new Date());
        testTask.setDuration(30);
        testTask.setPrice(20);


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

    @Test
    public void deleteTaskWithId_success() {

        long taskId = 1L;
        String token = "validToken";

        Mockito.when(taskRepository.findById(taskId)).thenReturn(testTask);
        Mockito.when(userService.getUserIdByToken("validToken")).thenReturn(testCreator.getId());

        taskService.deleteTaskWithId(taskId, token);

        Mockito.verify(taskRepository).delete(testTask);
    }

    @Test
    public void deleteTaskWithId_taskNotFound_throwsException() {

        long taskId = 5L;
        String token = "validToken";
        assertThrows(NoSuchElementException.class, () -> taskService.deleteTaskWithId(taskId, token));
    }

    @Test
    public void deleteTaskWithId_unauthorizedAccess_throwsException() {

        long taskId = 1L;
        String token = "invalidToken";
        Mockito.when(taskRepository.findById(taskId)).thenReturn(testTask);

        assertThrows(ResponseStatusException.class, () -> taskService.deleteTaskWithId(taskId, token),
                "only the creator of this task is allowed to delete it");
    }

    @Test
    public void getTasksByApplicant_returnsListOfTasks(){
        List<Task> expected = Collections.singletonList(testTask);
        Mockito.when(taskRepository.findTasksByApplicantId(Mockito.anyLong())).thenReturn(expected);

        long userId = 1L;

        List<Task> actual = taskService.getTasksByApplicant(userId);

        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).getAddress(), actual.get(0).getAddress());
        assertEquals(expected.get(0).getDuration(), actual.get(0).getDuration());
        assertEquals(expected.get(0).getDescription(), actual.get(0).getDescription());
        assertEquals(expected.get(0).getTitle(), actual.get(0).getTitle());
        assertEquals(expected.get(0).getCreator(), actual.get(0).getCreator());

        verify(taskRepository).findTasksByApplicantId(userId);
    }

    @Test
    public void getTasksByCreator_returnsListOfTasks(){
        List<Task> expected = Collections.singletonList(testTask);
        Mockito.when(taskRepository.findByCreatorId(Mockito.anyLong())).thenReturn(expected);

        long creatorId = 1L;

        List<Task> actual = taskService.getTasksByCreator(creatorId);

        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).getAddress(), actual.get(0).getAddress());
        assertEquals(expected.get(0).getDuration(), actual.get(0).getDuration());
        assertEquals(expected.get(0).getDescription(), actual.get(0).getDescription());
        assertEquals(expected.get(0).getTitle(), actual.get(0).getTitle());
        assertEquals(expected.get(0).getCreator(), actual.get(0).getCreator());

        verify(taskRepository).findByCreatorId(creatorId);
    }

    @Test
    public void getTasks_returnsListOfTasks(){
        List<Task> expected = Collections.singletonList(testTask);
        Mockito.when(taskRepository.findAll()).thenReturn(expected);

        List<Task> actual = taskService.getTasks();

        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).getAddress(), actual.get(0).getAddress());
        assertEquals(expected.get(0).getDuration(), actual.get(0).getDuration());
        assertEquals(expected.get(0).getDescription(), actual.get(0).getDescription());
        assertEquals(expected.get(0).getTitle(), actual.get(0).getTitle());
        assertEquals(expected.get(0).getCreator(), actual.get(0).getCreator());

        verify(taskRepository).findAll();
    }

    @Test
    public void getCandidates_returnsListOfUsers(){
        List<User> expected = Collections.singletonList(testCandidate);
        Mockito.when(userService.getCandidatesByTaskId(testCreator.getId())).thenReturn(expected);

        List<User> actual = taskService.getCandidatesForTaskWithId(testCreator.getId());

        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).getId(), actual.get(0).getId());
        assertEquals(expected.get(0).getName(), actual.get(0).getName());
        assertEquals(expected.get(0).getUsername(), actual.get(0).getUsername());
        assertEquals(expected.get(0).getCoinBalance(), actual.get(0).getCoinBalance());

        verify(userService).getCandidatesByTaskId(testCreator.getId());
    }




}
