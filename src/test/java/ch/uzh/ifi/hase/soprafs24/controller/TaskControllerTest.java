package ch.uzh.ifi.hase.soprafs24.controller;

//import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Task;
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
        //given
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
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }
}
