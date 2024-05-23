package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.TaskStatus;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.TaskPutDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

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
    private User testHelper;

    private Date date;

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

        LocalDate localDate = LocalDate.of(2025, 5, 25);  // Year, Month, Day
        LocalDateTime localDateTime = localDate.atTime(LocalTime.of(14, 30)); // Hour, Minute
        date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("testTitle");
        testTask.setDescription("testDescription");
        testTask.setAddress("testAddress");
        testTask.setCreator(testCreator);
        testTask.setDate(date);
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
    public void createTask_lowCreditBalance_throwsException() {

        testCreator.setCoinBalance(10);

        assertThrows(ResponseStatusException.class, () -> taskService.createTask(testTask, testCreator.getId()));
    }

    @Test
    public void deleteTaskWithId_success() {

        long taskId = 1L;
        String token = "validToken";

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.ofNullable(testTask));
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
        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.ofNullable(testTask));

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
        Mockito.when(taskRepository.findAllWithDateAfterOrEqual(any())).thenReturn(expected);

        List<Task> actual = taskService.getTasks();

        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).getAddress(), actual.get(0).getAddress());
        assertEquals(expected.get(0).getDuration(), actual.get(0).getDuration());
        assertEquals(expected.get(0).getDescription(), actual.get(0).getDescription());
        assertEquals(expected.get(0).getTitle(), actual.get(0).getTitle());
        assertEquals(expected.get(0).getCreator(), actual.get(0).getCreator());
    }

    @Test
    public void getTasks_excludesHistoricalTasks() {

        Task historicalTask = new Task();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -5);
        historicalTask.setDate(cal.getTime());

        List<Task> expected = Collections.singletonList(testTask);
        Date today = new Date();
        Mockito.when(taskRepository.findAllWithDateAfterOrEqual(today)).thenReturn(expected);

        List<Task> actual = taskService.getTasks();

        assertEquals(1, actual.size());
        assertFalse(actual.contains(historicalTask), "Historical tasks should not be returned.");
        assertTrue(actual.contains(testTask), "Expected task is not returned.");

        verify(taskRepository).findAllWithDateAfterOrEqual(today);
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

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.ofNullable(testTask));
        Mockito.when(userService.getUserByToken(token)).thenReturn(testCandidate);

        taskService.deleteCandidate(taskId, token);

        assertFalse(testTask.getCandidates().contains(testCandidate), "Candidate should be removed from the task");
        assertFalse(testCandidate.getApplications().contains(testTask), "Task should be removed from the candidate's applications");
        Mockito.verify(taskRepository).save(testTask);
        Mockito.verify(userService).saveUser(testCandidate);
    }

    @Test
    public void deleteCandidate_NotFoundInTask_ThrowsException() {
        long taskId = 1L;
        String token = "validToken";
        List<User> candidates = Collections.emptyList();

        testTask.setCandidates(candidates);

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.ofNullable(testTask));
        Mockito.when(userService.getUserByToken(token)).thenReturn(testCandidate);

        assertThrows(ResponseStatusException.class, () -> taskService.deleteCandidate(taskId, token),
                "Should throw exception as the user is not a candidate for the task");

        Mockito.verify(taskRepository, never()).save(any(Task.class));
        Mockito.verify(userService, never()).saveUser(any(User.class));
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

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(testTask));
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

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(testTask));
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

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(testTask));
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

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(testTask));
        Mockito.when(userService.getUserIdByToken(unauthorizedToken)).thenReturn(999L); // Some unrelated user

        assertThrows(ResponseStatusException.class, () -> taskService.confirmTask(taskId, unauthorizedToken));
    }

    @Test
    public void confirmTask_AlreadyConfirmed_ThrowsBadRequest() {
        long taskId = 1L;
        String creatorToken = "creatorToken";
        Task testTask = new Task();
        testTask.setStatus(TaskStatus.CONFIRMED_BY_CREATOR);
        testTask.setCreator(testCreator);
        testTask.setHelper(testHelper);

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(testTask));
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
        candidate.setApplications(new ArrayList<>(Collections.emptyList()));

        Task task = new Task();
        task.setId(1L);


        when(userService.getUserByToken(anyString())).thenReturn(candidate);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        taskService.apply(taskPutDTO, "validToken");
        assertTrue(task.getCandidates().contains(candidate));
    }

    @Test
    public void apply_invalidToken_throwsForbidden() {
        TaskPutDTO taskPutDTO = new TaskPutDTO();
        taskPutDTO.setUserId(1L);
        taskPutDTO.setTaskId(1L);

        when(userService.getUserByToken(anyString())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> taskService.apply(taskPutDTO, "invalidToken"));
    }

    @Test
    public void apply_taskNotFound_throwsNotFound() {
        TaskPutDTO taskPutDTO = new TaskPutDTO();
        taskPutDTO.setUserId(1L);
        taskPutDTO.setTaskId(1L);

        User candidate = new User();
        candidate.setId(1L);

        when(userService.getUserByToken(anyString())).thenReturn(candidate);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

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
        task.setCandidates(new ArrayList<>(Collections.singletonList(candidate)));

        when(userService.getUserByToken(anyString())).thenReturn(candidate);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        assertThrows(ResponseStatusException.class, () -> taskService.apply(taskPutDTO, "validToken"));
    }

    @Test
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
        task.setCandidates(new ArrayList<>(Collections.singletonList(helper)));

        when(userService.getUserById(anyLong())).thenReturn(helper, creator);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        when(userService.getUserByToken("validToken")).thenReturn(creator);

        taskService.selectCandidate(taskPutDTO, "validToken");

        assertEquals(helper, task.getHelper());
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
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

        when(userService.getUserById(anyLong())).thenReturn(creator);

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

        when(userService.getUserById(anyLong())).thenReturn(creator);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

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

        when(userService.getUserById(anyLong())).thenReturn(helper, creator);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        assertThrows(ResponseStatusException.class, () -> taskService.selectCandidate(taskPutDTO, "validToken"));
    }

    @Test
    public void clearOtherCandidates_validInputs_applicationsDeleted() {
        User helper1 = new User();
        helper1.setId(1L);

        User helper2 = new User();
        helper2.setId(2L);

        Task task = new Task();
        task.setId(1L);

        task.setCandidates(new ArrayList<>(Arrays.asList(helper1, helper2)));
        helper1.setApplications(new ArrayList<>(Collections.singletonList(task)));
        helper2.setApplications(new ArrayList<>(Collections.singletonList(task)));

        taskService.clearOtherCandidates(task, helper1);
        assertEquals(1, task.getCandidates().size());
        assertTrue(task.getCandidates().contains(helper1));
        assertFalse(task.getCandidates().contains(helper2));

    }

    @Test
    public void deleteTaskWithId_removesTaskFromCandidatesApplicationsAndSavesUpdatedCandidate() {

        long taskId = 1L;
        String token = "validToken";

        List<User> candidates = new ArrayList<>();
        candidates.add(testCandidate);
        testTask.setCandidates(candidates);

        List<Task> applications = new ArrayList<>();
        applications.add(testTask);
        testCandidate.setApplications(applications);

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(testTask));
        Mockito.when(userService.getUserIdByToken(token)).thenReturn(testCreator.getId());

        taskService.deleteTaskWithId(taskId, token);

        assertFalse(testCandidate.getApplications().contains(testTask), "The task should be removed from the candidate's applications");

        Mockito.verify(userService).saveUser(testCandidate);

        Mockito.verify(taskRepository).delete(testTask);
    }

}
