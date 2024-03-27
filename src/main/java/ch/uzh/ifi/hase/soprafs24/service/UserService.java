package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.TaskStatus;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public UserService(@Qualifier("userRepository") UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
}
  

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }

  public User createUser(User newUser) {
    checkIfUserExists(newUser);
    newUser.setToken(UUID.randomUUID().toString());
    newUser.setStatus(UserStatus.ONLINE);
    String encodedPassword = bCryptPasswordEncoder.encode(newUser.getPassword());
    newUser.setPassword(encodedPassword);
    newUser = userRepository.save(newUser);
    userRepository.flush();
    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

  public User login (User userInput) {
    User userRetrieved = userRepository.findByUsername(userInput.getUsername());

    String baseErrorMessage = "The username: %s was not found in the database!";
    if (userRetrieved == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
      String.format(baseErrorMessage, userInput.getUsername()));
    }

    String passwordRetrieved = userRetrieved.getPassword();
    String passwordInput = userInput.getPassword();

    if (!passwordInput.equals(passwordRetrieved)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The password was not correct."));
    }
    userRetrieved.setToken(UUID.randomUUID().toString());
    userRetrieved.setStatus(UserStatus.ONLINE);
    this.userRepository.saveAndFlush(userRetrieved);
    return userRetrieved;
  }

  public void logOut(String token) {
    tokenExistance(token);
    User userRetrieved = this.userRepository.findUserByToken(token);
    if (userRetrieved == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Can't find the user."));
    }
    userRetrieved.setToken(null);
    userRetrieved.setStatus(UserStatus.OFFLINE);
    this.userRepository.save(userRetrieved);
  }

  private void checkIfUserExists(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

    String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
    if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
          String.format(baseErrorMessage, userToBeCreated.getUsername(), "is"));
    }
  }

    // Check if the token is of the right user

  // Gets as an input a String token, an User instance and an error message to display. 
  // If the token retrieved by the user entity is different from the token provided it throws an exception
  private Boolean tokenValidation(User user, String tokenProvidedbyClient, String errorMessage) {
    if (!tokenProvidedbyClient.equals(user.getToken())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, errorMessage);
    }
    return true;
  }


  // Check if a token is valid
  // Gets as an input a String token, thanks to the userRepository’s method findUserByToken retrieves an user.
  // If the user is null it throws an exception.
  private void tokenExistance(String token) {
    if (this.userRepository.findUserByToken(token) == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You don't have a valid token.");
    }
  }
}
