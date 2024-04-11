package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RatingPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserEditDTO;
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

  @PostMapping("/users")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO, HttpServletResponse response) {
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
    User createdUser = this.userService.createUser(userInput);
    response.addHeader("Authorization", createdUser.getToken());
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
  }

    @PutMapping("/users") //QUESTION the path should be /login
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO loginUser(@RequestBody UserPutDTO userPutDTO, HttpServletResponse response) {
      User userInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
      User userRetrieved = this.userService.login(userInput);
      UserGetDTO userSentToClient = DTOMapper.INSTANCE.convertEntityToUserGetDTO(userRetrieved);
      response.addHeader("Authorization", userRetrieved.getToken());
      return userSentToClient;
    }

    @PutMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void logout(@RequestHeader("Authorization") String token) {
      this.userService.logOut(token);
    }

    @PutMapping("/users/{id}") //QUESTION This logically conflicts with PUT login, even though it would work
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editProfile(@PathVariable Long id, @RequestBody UserEditDTO userEditDTO,
        @RequestHeader("Authorization") String token) {
      User userWithPendingChanges = DTOMapper.INSTANCE.convertUserEditDTOToEntity(userEditDTO);
      this.userService.editProfile(userWithPendingChanges, token, id);
    }

  @GetMapping("/users/{userId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO getUser(@PathVariable("userId") long id) {
    User user = userService.getUserById(id);
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
  }

    @PostMapping("/ratings/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void creatRating(@RequestBody RatingPostDTO ratingPostDTO) {
        // TODO: implement
    }



}
