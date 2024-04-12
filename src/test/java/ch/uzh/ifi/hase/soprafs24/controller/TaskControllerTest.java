package ch.uzh.ifi.hase.soprafs24.controller;

//import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Task;
import ch.uzh.ifi.hase.soprafs24.entity.User;
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
import java.util.NoSuchElementException;

import java.util.Collections;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import static org.hamcrest.Matchers.equalTo;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

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
        //Mockito.doNothing().when(taskService).createTask(Mockito.any(), Mockito.eq(1));

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

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }
}
