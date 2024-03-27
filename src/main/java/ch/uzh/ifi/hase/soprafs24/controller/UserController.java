package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RatingPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController {

  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }
    //QUESTION: Check surname field
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO, HttpServletResponse response) {
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        User createdUser = userService.createUser(userInput);
        response.addHeader("Authorization", createdUser.getToken());
        UserGetDTO userSentToClient = DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
        return userSentToClient;
    } 

    @PutMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO loginUser(@RequestBody UserPutDTO userPutDTO, HttpServletResponse response) {
        User userInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
        User userRetrieved = userService.login(userInput);
        response.addHeader("Authorization", userRetrieved.getToken());
        UserGetDTO userSentToClient = DTOMapper.INSTANCE.convertEntityToUserGetDTO(userRetrieved);
        return userSentToClient;
    }

    @PutMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void logoutUser(@RequestHeader("Authorization") String token) {
      this.userService.logOut(token);
    }
  

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateUser(@PathVariable("id") long id) {
        // TODO: implement
    }

  @GetMapping("/users/{userId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO getUser(@PathVariable("userId") long id) {
    // TODO: implement
    return new UserGetDTO();
  }

    @PostMapping("/ratings/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void createRating(@RequestBody RatingPostDTO ratingPostDTO) {
        // TODO: implement
    }



}
