package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

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
  public UserGetFullDTO getUser(@PathVariable("userId") long id) {
    User user = userService.getUserById(id);
    return DTOMapper.INSTANCE.convertEntityToUserGetFullDTO(user);
  }

    @GetMapping("/leaderboard")
    public List<UserTaskCountDTO> getLeaderboard() {
        List<Object[]> rankedUsers = userService.getRankedUsers();
        return rankedUsers.stream()
                .map(objects -> DTOMapper.INSTANCE.toUserTaskCountDTO(
                        (User) objects[0],
                        (Long) objects[1],
                        (Integer) objects[2])
                )
                .collect(Collectors.toList());
    }

}
