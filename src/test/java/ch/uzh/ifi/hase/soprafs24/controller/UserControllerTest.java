package ch.uzh.ifi.hase.soprafs24.controller;
import static org.hamcrest.Matchers.equalTo;

import ch.uzh.ifi.hase.soprafs24.constant.TaskStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @Test
  public void givenUser_whenGetUserById_thenReturnUser() throws Exception {

    User user = new User();
    user.setName("Firstname Lastname");
    user.setUsername("firstname@lastname");
    user.setRadius(20L);
    user.setCoinBalance(50);
    user.setAddress("address");
    user.setPhoneNumber("000");
    user.setId(1L);
    user.setPassword("password");

    Mockito.when(userService.getUserWithRatings(Mockito.anyLong())).thenReturn(user);

    MockHttpServletRequestBuilder getRequest = get("/users/1").contentType(MediaType.APPLICATION_JSON);

    mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is(user.getName())))
        .andExpect(jsonPath("$.coinBalance", is(user.getCoinBalance())))
        .andExpect(jsonPath("$.address", is(user.getAddress())))
        .andExpect(jsonPath("$.phoneNumber", is(user.getPhoneNumber())))
        .andExpect(jsonPath("$.radius", is(20.0)));
}

    @Test
    public void invalidId_whenGetUserBy_thenThrowNotFound() throws Exception {

        Mockito.when(userService.getUserWithRatings(Mockito.anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder getRequest = get("/users/1").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isNotFound());
  }

  @Test
  public void createUser_validInput_userCreated() throws Exception {
    // given
    User user = new User();
    user.setId(1L);
    user.setName("Test User");
    user.setUsername("testUsername");
    user.setToken("1");

    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setName("Test User");
    userPostDTO.setUsername("testUsername");

    given(userService.createUser(Mockito.any())).willReturn(user);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userPostDTO));

    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(user.getId().intValue())))
        .andExpect(jsonPath("$.name", is(user.getName())))
        .andExpect(jsonPath("$.username", is(user.getUsername())));
  }

    @Test
    public void getLeaderboard_success() throws Exception {

        User user1 = new User();
        user1.setId(1L);
        user1.setName("User One");
        User user2 = new User();
        user2.setId(2L);
        user2.setName("User Two");

        List<Object[]> rankedUsers = Arrays.asList(
                new Object[] {user1, 10L, 1},
                new Object[] {user2, 5L, 2}
        );

        Mockito.when(userService.getRankedUsers()).thenReturn(rankedUsers);

        mockMvc.perform(get("/leaderboard").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].taskCount", is(10)))
                .andExpect(jsonPath("$[0].rank", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].taskCount", is(5)))
                .andExpect(jsonPath("$[1].rank", is(2)));
    }

    @Test
    public void getLeaderboard_emptyList() throws Exception {

        List<Object[]> rankedUsers = Collections.emptyList();

        Mockito.when(userService.getRankedUsers()).thenReturn(rankedUsers);


        mockMvc.perform(get("/leaderboard").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

  /**
   * Helper Method to convert userPostDTO into a JSON string such that the input
   * can be processed
   * Input will look like this: {"name": "Test User", "username": "testUsername"}
   * 
   * @param object
   * @return string
   */
  private String asJsonString(final Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format("The request body could not be created.%s", e.toString()));
    }
  }
}