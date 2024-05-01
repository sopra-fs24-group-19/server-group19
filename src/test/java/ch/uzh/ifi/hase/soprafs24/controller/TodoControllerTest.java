package ch.uzh.ifi.hase.soprafs24.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import ch.uzh.ifi.hase.soprafs24.entity.Todo;
import ch.uzh.ifi.hase.soprafs24.rest.dto.TodoPostDTO;
import ch.uzh.ifi.hase.soprafs24.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.boot.test.mock.mockito.MockBean;
@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void createTodo_success() throws Exception {
        TodoPostDTO todoPostDTO = new TodoPostDTO();
        todoPostDTO.setDescription("test description");
        String token = "test-token";

        doNothing().when(todoService).createTodo(any(Todo.class), anyLong(), anyString());

        mockMvc.perform(post("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(todoPostDTO)))
                .andExpect(status().isCreated());
        verify(todoService, times(1)).createTodo(any(Todo.class), anyLong(), anyString());
    }

    @Test
    public void createTodo_fail_unauthorized() throws Exception {
        TodoPostDTO todoPostDTO = new TodoPostDTO();
        String token = "invalid-token";

        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"))
                .when(todoService).createTodo(any(Todo.class), anyLong(), anyString());

        mockMvc.perform(post("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(todoPostDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void updateTodo_success() throws Exception {
        TodoPostDTO todoPostDTO = new TodoPostDTO();
        long id = 1L;
        String token = "test-token";

        doNothing().when(todoService).updateTodo(any(Todo.class), anyString(), eq(id));

        mockMvc.perform(put("/todo/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(todoPostDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateTodo_fail_notFound() throws Exception {
        TodoPostDTO todoPostDTO = new TodoPostDTO();
        long id = 1L;
        String token = "test-token";

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found"))
                .when(todoService).updateTodo(any(Todo.class), anyString(), eq(id));

        mockMvc.perform(put("/todo/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(todoPostDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAreAllTodosDone() throws Exception {
        Long taskId = 1L;
        boolean allTodosDone = true;

        when(todoService.areAllTodosDone(taskId)).thenReturn(allTodosDone);

        mockMvc.perform(get("/allTodosDone/{taskId}", taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(todoService, times(1)).areAllTodosDone(taskId);
    }
}
