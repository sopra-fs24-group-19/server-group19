package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.TaskStatus;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.*;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.TaskPutDTO;
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
    private ApplicationsRepository applicationsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskService taskService;

    private Task testTask;
    private User testCreator;
    private User testCandidate;

    private User testHelper;

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

        testHelper = new User();
        testHelper.setCoinBalance(50);
        testHelper.setId(2L);
        testHelper.setName("helperName");
        testHelper.setUsername("helperUsername");

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

    //@Test
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
    public void createTask_lowCreditBalance_throwsException() {

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
    public void getTasksByApplicant_returnsListOfTasks() {
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
    public void getTasksByCreator_returnsListOfTasks() {
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
    public void getTasks_returnsListOfTasks() {
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
    public void getCandidates_returnsListOfUsers() {
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

    @Test
    public void deleteCandidate_Success() {
        long taskId = 1L;
        String token = "validToken";

        List<User> candidates = new ArrayList<>();
        candidates.add(testCandidate);

        List<Task> applications = new ArrayList<>();
        applications.add(testTask);
        testCandidate.setApplications(applications);

        testTask.setCandidates(candidates);
        testCandidate.setApplications(applications);

        Mockito.when(taskRepository.findById(taskId)).thenReturn(testTask);
        Mockito.when(userRepository.findByToken(token)).thenReturn(testCandidate);

        taskService.deleteCandidate(taskId, token);

        assertFalse(testTask.getCandidates().contains(testCandidate), "Candidate should be removed from the task");
        assertFalse(testCandidate.getApplications().contains(testTask), "Task should be removed from the candidate's applications");
        Mockito.verify(taskRepository).save(testTask);
        Mockito.verify(userRepository).save(testCandidate);
    }

    @Test
    public void deleteCandidate_NotFoundInTask_ThrowsException() {
        long taskId = 1L;
        String token = "validToken";
        List<User> candidates = Collections.emptyList();

        testTask.setCandidates(candidates);

        Mockito.when(taskRepository.findById(taskId)).thenReturn(testTask);
        Mockito.when(userRepository.findByToken(token)).thenReturn(testCandidate);

        assertThrows(ResponseStatusException.class, () -> taskService.deleteCandidate(taskId, token),
                "Should throw exception as the user is not a candidate for the task");

        Mockito.verify(taskRepository, never()).save(any(Task.class));
        Mockito.verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void confirmTask_Success_CreatorConfirmsFirst() {
        long taskId = 1L;
        String creatorToken = "creatorToken";
        Task testTask = new Task();
        testTask.setStatus(TaskStatus.CREATED);
        testTask.setCreator(testCreator);
        testTask.setHelper(testHelper);
        testTask.setPrice(100);

        Mockito.when(taskRepository.findById(taskId)).thenReturn(testTask);
        Mockito.when(userService.getUserIdByToken(creatorToken)).thenReturn(testCreator.getId());

        Task confirmedTask = taskService.confirmTask(taskId, creatorToken);

        assertEquals(TaskStatus.CONFIRMED_BY_CREATOR, confirmedTask.getStatus());
        Mockito.verify(taskRepository).save(testTask);
    }

    @Test
    public void confirmTask_Success_HelperConfirmsFirst() {
        long taskId = 1L;
        String helperToken = "helperToken";
        Task testTask = new Task();
        testTask.setStatus(TaskStatus.CREATED);
        testTask.setCreator(testCreator);
        testTask.setHelper(testHelper);
        testTask.setPrice(100);

        Mockito.when(taskRepository.findById(taskId)).thenReturn(testTask);
        Mockito.when(userService.getUserIdByToken(helperToken)).thenReturn(testHelper.getId());

        Task confirmedTask = taskService.confirmTask(taskId, helperToken);

        assertEquals(TaskStatus.CONFIRMED_BY_HELPER, confirmedTask.getStatus());
        Mockito.verify(taskRepository).save(testTask);
    }

    @Test
    public void confirmTask_Success_BothConfirmTaskDone() {
        long taskId = 1L;
        String creatorToken = "creatorToken";
        Task testTask = new Task();
        testTask.setStatus(TaskStatus.CONFIRMED_BY_HELPER); // Helper has already confirmed
        testTask.setCreator(testCreator);
        testTask.setHelper(testHelper);
        testTask.setPrice(100);

        Mockito.when(taskRepository.findById(taskId)).thenReturn(testTask);
        Mockito.when(userService.getUserIdByToken(creatorToken)).thenReturn(testCreator.getId());

        Task confirmedTask = taskService.confirmTask(taskId, creatorToken);

        assertEquals(TaskStatus.DONE, confirmedTask.getStatus());
        Mockito.verify(userService).addCoins(testHelper, 100);
        Mockito.verify(taskRepository).save(testTask);
    }

    @Test
    public void confirmTask_Unauthorized_User() {
        long taskId = 1L;
        String unauthorizedToken = "unauthorizedToken";
        Task testTask = new Task();
        testTask.setCreator(testCreator);
        testTask.setHelper(testHelper);

        Mockito.when(taskRepository.findById(taskId)).thenReturn(testTask);
        Mockito.when(userService.getUserIdByToken(unauthorizedToken)).thenReturn(999L); // Some unrelated user

        assertThrows(ResponseStatusException.class, () -> taskService.confirmTask(taskId, unauthorizedToken));
    }

    @Test
    public void confirmTask_AlreadyConfirmed_ThrowsBadRequest() {
        long taskId = 1L;
        String creatorToken = "creatorToken";
        Task testTask = new Task();
        testTask.setStatus(TaskStatus.CONFIRMED_BY_CREATOR); // Creator has already confirmed
        testTask.setCreator(testCreator);
        testTask.setHelper(testHelper);

        Mockito.when(taskRepository.findById(taskId)).thenReturn(testTask);
        Mockito.when(userService.getUserIdByToken(creatorToken)).thenReturn(testCreator.getId());

        assertThrows(ResponseStatusException.class, () -> taskService.confirmTask(taskId, creatorToken),
                "You have already confirmed this task.");
    }

    @Test
    public void apply_validInputs_applicationCreated() {
        TaskPutDTO taskPutDTO = new TaskPutDTO();
        taskPutDTO.setUserId(1L);
        taskPutDTO.setTaskId(1L);

        User candidate = new User();
        candidate.setId(1L);

        Task task = new Task();
        task.setId(1L);

        when(userRepository.findUserByToken(anyString())).thenReturn(candidate);
        when(taskRepository.findById(anyLong())).thenReturn(task);
        when(applicationsRepository.findByUserAndTask(any(User.class), any(Task.class))).thenReturn(null);

        taskService.apply(taskPutDTO, "validToken");

        verify(applicationsRepository, times(1)).saveAndFlush(any(Application.class));
    }

    @Test
    public void apply_invalidToken_throwsForbidden() {
        TaskPutDTO taskPutDTO = new TaskPutDTO();
        taskPutDTO.setUserId(1L);
        taskPutDTO.setTaskId(1L);

        when(userRepository.findUserByToken(anyString())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> taskService.apply(taskPutDTO, "invalidToken"));
    }

    @Test
    public void apply_taskNotFound_throwsNotFound() {
        TaskPutDTO taskPutDTO = new TaskPutDTO();
        taskPutDTO.setUserId(1L);
        taskPutDTO.setTaskId(1L);

        User candidate = new User();
        candidate.setId(1L);

        when(userRepository.findUserByToken(anyString())).thenReturn(candidate);
        when(taskRepository.findById(anyLong())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> taskService.apply(taskPutDTO, "validToken"));
    }

    @Test
    public void apply_alreadyApplied_throwsConflict() {
        TaskPutDTO taskPutDTO = new TaskPutDTO();
        taskPutDTO.setUserId(1L);
        taskPutDTO.setTaskId(1L);

        User candidate = new User();
        candidate.setId(1L);

        Task task = new Task();
        task.setId(1L);

        Application existingApplication = new Application();

        when(userRepository.findUserByToken(anyString())).thenReturn(candidate);
        when(taskRepository.findById(anyLong())).thenReturn(task);
        when(applicationsRepository.findByUserAndTask(any(User.class), any(Task.class))).thenReturn(existingApplication);

        assertThrows(ResponseStatusException.class, () -> taskService.apply(taskPutDTO, "validToken"));
    }

    //@Test
    public void selectCandidate_validInputs_taskInProgress() {
        TaskPutDTO taskPutDTO = new TaskPutDTO();
        taskPutDTO.setUserId(1L);
        taskPutDTO.setTaskId(1L);
        taskPutDTO.setHelperId(2L);

        User creator = new User();
        creator.setId(1L);
        creator.setToken("validToken");

        User helper = new User();
        helper.setId(2L);

        Task task = new Task();
        task.setId(1L);

        Application application = new Application();
        application.setUser(helper);
        application.setTask(task);

        when(userRepository.findUserById(anyLong())).thenReturn(helper, creator);
        when(taskRepository.findById(anyLong())).thenReturn(task);
        when(applicationsRepository.findByUserAndTask(any(User.class), any(Task.class))).thenReturn(application);
        when(userRepository.findUserByToken("validToken")).thenReturn(creator); // Ensure the token matches

        taskService.selectCandidate(taskPutDTO, "validToken");

        assertEquals(helper, task.getHelper());
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        verify(taskService, times(1)).deleteApplicationsByTask(task, helper);
    }

    @Test
    public void selectCandidate_invalidToken_throwsForbidden() {
        TaskPutDTO taskPutDTO = new TaskPutDTO();

        taskPutDTO.setUserId(1L);
        taskPutDTO.setTaskId(1L);
        taskPutDTO.setHelperId(2L);

        User creator = new User();
        creator.setId(1L);
        creator.setToken("invalidToken");

        when(userRepository.findUserById(anyLong())).thenReturn(creator);

        assertThrows(ResponseStatusException.class, () -> taskService.selectCandidate(taskPutDTO, "validToken"));
    }

    @Test
    public void selectCandidate_taskNotFound_throwsNotFound() {
        TaskPutDTO taskPutDTO = new TaskPutDTO();
        taskPutDTO.setUserId(1L);
        taskPutDTO.setTaskId(1L);
        taskPutDTO.setHelperId(2L);

        User creator = new User();
        creator.setId(1L);
        creator.setToken("validToken");

        when(userRepository.findUserById(anyLong())).thenReturn(creator);
        when(taskRepository.findById(anyLong())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> taskService.selectCandidate(taskPutDTO, "validToken"));
    }

    @Test
    public void selectCandidate_applicationNotFound_throwsUnauthorized() {
        TaskPutDTO taskPutDTO = new TaskPutDTO();
        taskPutDTO.setUserId(1L);
        taskPutDTO.setTaskId(1L);
        taskPutDTO.setHelperId(2L);

        User creator = new User();
        creator.setId(1L);
        creator.setToken("validToken");

        User helper = new User();
        helper.setId(2L);

        Task task = new Task();
        task.setId(1L);

        when(userRepository.findUserById(anyLong())).thenReturn(helper, creator);
        when(taskRepository.findById(anyLong())).thenReturn(task);
        when(applicationsRepository.findByUserAndTask(any(User.class), any(Task.class))).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> taskService.selectCandidate(taskPutDTO, "validToken"));
    }

    @Test
    public void deleteApplicationsByTask_validInputs_applicationsDeleted() {
        User helper = new User();
        helper.setId(1L);

        Task task = new Task();
        task.setId(1L);

        Application application1 = new Application();
        application1.setUser(helper);
        application1.setTask(task);

        Application application2 = new Application();
        application2.setUser(helper);
        application2.setTask(task);

        List<Application> applicationList = Arrays.asList(application1, application2);

        when(applicationsRepository.findApplicationsByTaskIdExcludingHelperId(anyLong(), anyLong())).thenReturn(applicationList);

        taskService.deleteApplicationsByTask(task, helper);

        verify(applicationsRepository, times(applicationList.size())).delete(any(Application.class));
    }
}
