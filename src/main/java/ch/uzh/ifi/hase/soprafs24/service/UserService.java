package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.TaskStatus;
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

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  @Autowired
  public UserService(@Qualifier("userRepository") UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }

  public User createUser(User newUser) {
    newUser.setToken(UUID.randomUUID().toString());
    checkIfUserExists(newUser);
    newUser = userRepository.save(newUser);
    userRepository.flush();
    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

  private void checkIfUserExists(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
    User userByName = userRepository.findByName(userToBeCreated.getName());

    String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
    if (userByUsername != null && userByName != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format(baseErrorMessage, "username and the name", "are"));
    } else if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "username", "is"));
    } else if (userByName != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "name", "is"));
    }
  }
}
