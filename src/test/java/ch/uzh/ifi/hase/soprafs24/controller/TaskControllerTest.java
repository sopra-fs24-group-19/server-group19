package ch.uzh.ifi.hase.soprafs24.controller;

//import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Task;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.TaskRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.TaskPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.service.TaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.Optional;

import java.text.SimpleDateFormat;

import static org.hamcrest.Matchers.equalTo;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private TaskRepository taskRepository;

    @Test
    public void createTask_validInput_taskCreated() throws Exception {
        Task task = new Task();

        TaskPostDTO taskPostDTO = new TaskPostDTO();
        taskPostDTO.setTitle("title");
        taskPostDTO.setDate(new Date());
        taskPostDTO.setDescription("description");
        taskPostDTO.setDuration(30);
        taskPostDTO.setAddress("address");
        taskPostDTO.setCompensation(50);
        taskPostDTO.setCreatorId(1);

        Mockito.when(taskService.createTask(Mockito.any(), Mockito.anyLong())).thenReturn(task);

        MockHttpServletRequestBuilder postRequest = post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(taskPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isCreated());
       }
    @Test
    public void createTask_invalidInput_lowCreditBalance() throws Exception{

        TaskPostDTO taskPostDTO = new TaskPostDTO();
        taskPostDTO.setTitle("title");
        taskPostDTO.setDate(new Date());
        taskPostDTO.setDescription("description");
        taskPostDTO.setDuration(30);
        taskPostDTO.setAddress("address");
        taskPostDTO.setCompensation(50);
        taskPostDTO.setCreatorId(1);

        Mockito.doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request"))
                .when(taskService).createTask(Mockito.any(), Mockito.anyLong());

        MockHttpServletRequestBuilder postRequest = post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(taskPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isBadRequest());
}

    @Test
    public void givenTasks_whenGetTasks_thenReturnJsonArray() throws Exception {

        User testCreator = new User();
        testCreator.setCoinBalance(50);
        testCreator.setId(1L);

        Task testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("testTitle");
        testTask.setDescription("testDescription");
        testTask.setAddress("testAddress");
        testTask.setCreator(testCreator);
        testTask.setDate(new Date());
        testTask.setDuration(30);
        testTask.setPrice(20);

        List<Task> allTasks = Collections.singletonList(testTask);

        Mockito.when(taskService.getTasks()).thenReturn(allTasks);
        MockHttpServletRequestBuilder getRequest = get("/tasks").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(testTask.getTitle())))
                .andExpect(jsonPath("$[0].description", is(testTask.getDescription())))
                .andExpect(jsonPath("$[0].address", is(testTask.getAddress())))
                .andExpect(jsonPath("$[0].date").exists()) // Check if 'date' exists, value check might need formatting
                .andExpect(jsonPath("$[0].duration", is(testTask.getDuration())))
                .andExpect(jsonPath("$[0].compensation", is(testTask.getPrice())));
    }

    @Test
    public void givenTaskId_getCandidates() throws Exception {
        User user = new User();
        user.setCoinBalance(50);
        user.setId(1L);
        user.setUsername("username");
        user.setName("name");
        long id = user.getId();

        List<User> candidates = Collections.singletonList(user);
        Mockito.when(taskService.getCandidatesForTaskWithId(Mockito.anyLong())).thenReturn(candidates);

        MockHttpServletRequestBuilder getRequest = get("/candidates/1").contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(user.getName())))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())));
    }

    @Test
    public void givenUserId_getCreatedTasks() throws Exception {
        User testCreator = new User();
        testCreator.setCoinBalance(50);
        testCreator.setId(1L);

        Task testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("testTitle");
        testTask.setDescription("testDescription");
        testTask.setAddress("testAddress");
        testTask.setCreator(testCreator);
        testTask.setDate(new Date());
        testTask.setDuration(30);
        testTask.setPrice(20);

        List<Task> tasks = Collections.singletonList(testTask);
        Mockito.when(taskService.getTasksByCreator(Mockito.anyLong())).thenReturn(tasks);

        MockHttpServletRequestBuilder getRequest = get("/tasks/created/1").contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(testTask.getTitle())))
                .andExpect(jsonPath("$[0].description", is(testTask.getDescription())))
                .andExpect(jsonPath("$[0].address", is(testTask.getAddress())))
                .andExpect(jsonPath("$[0].date").exists())
                .andExpect(jsonPath("$[0].duration", is(testTask.getDuration())))
                .andExpect(jsonPath("$[0].compensation", is(testTask.getPrice())));
    }

    @Test
    public void givenUserId_getAppliedTasks() throws Exception {
        User testCreator = new User();
        testCreator.setCoinBalance(50);
        testCreator.setId(1L);

        Task testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("testTitle");
        testTask.setDescription("testDescription");
        testTask.setAddress("testAddress");
        testTask.setCreator(testCreator);
        testTask.setDate(new Date());
        testTask.setDuration(30);
        testTask.setPrice(20);

        List<Task> tasks = Collections.singletonList(testTask);
        Mockito.when(taskService.getTasksByApplicant(Mockito.anyLong())).thenReturn(tasks);

        MockHttpServletRequestBuilder getRequest = get("/tasks/appliedfor/1").contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(testTask.getTitle())))
                .andExpect(jsonPath("$[0].description", is(testTask.getDescription())))
                .andExpect(jsonPath("$[0].address", is(testTask.getAddress())))
                .andExpect(jsonPath("$[0].date").exists())
                .andExpect(jsonPath("$[0].duration", is(testTask.getDuration())))
                .andExpect(jsonPath("$[0].compensation", is(testTask.getPrice())));
    }

    @Test
    public void deleteTask_success() throws Exception {
        long taskId = 1L;
        Task mockTask = new Task();
        mockTask.setId(taskId);
        mockTask.setCreator(new User());
        doNothing().when(taskRepository).delete(mockTask);

        mockMvc.perform(delete("/tasks/{taskId}", taskId)
                        .header("AuthorizationToken", "validToken"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteTask_fail() throws Exception {
        long taskId = 1L;
        Task mockTask = new Task();
        mockTask.setId(taskId);
        mockTask.setCreator(new User());
        Mockito.doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED))
                .when(taskService).deleteTaskWithId(Mockito.anyLong(), Mockito.anyString());

        mockMvc.perform(delete("/tasks/{taskId}", taskId)
                        .header("AuthorizationToken", "inValidToken"))
                .andExpect(status().isUnauthorized());
    }



    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }
}
