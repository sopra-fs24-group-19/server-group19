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
import java.util.NoSuchElementException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

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
    newUser.setStatus(UserStatus.ONLINE);
    checkIfUserExists(newUser);
    newUser = this.userRepository.save(newUser);
    this.userRepository.flush();
    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

  public User login(User userLogin) {
    User userRetrieved = this.userRepository.findByUsername(userLogin.getUsername());
    if (userRetrieved == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format("The username you provided was not found in the database."));
    }
    String userLoginPassword = userLogin.getPassword();
    String userRetrievedPassword = userRetrieved.getPassword();
    if (!userLoginPassword.equals(userRetrievedPassword)) {
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
    this.userRepository.saveAndFlush(userRetrieved);
  }

  public void editProfile(User userWithPendingChanges, String tokenProvidedbyClient, Long id) {
    tokenExistance(tokenProvidedbyClient);
    User userToEdit = this.userRepository.findUserById(id);
    if (userToEdit == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("The user with userId: %d was not found.", id));
    }
    tokenValidation(userToEdit, tokenProvidedbyClient, "Token is invalid.");
    userToEdit.setName(userWithPendingChanges.getName());
    userToEdit.setAddress(userWithPendingChanges.getAddress());
    userToEdit.setPhoneNumber(userWithPendingChanges.getPhoneNumber());
    userToEdit.setRadius(userWithPendingChanges.getRadius());
    //TODO SPECIFY ALL THE VARIABLES IN WHICH WE ALLOW UPDATES i.e. we don't allow to change the username
    this.userRepository.saveAndFlush(userToEdit);
  }

  private void checkIfUserExists(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
    String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
    if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "username", "is"));
    }
  }

    public User getUserById(long id) {
        Optional<User> user = this.userRepository.findById(id);
        if(!user.isPresent()){
            throw new NoSuchElementException("User not found with id: " + id);

        } else {
            return user.get();
        }

    }
    public void subtractCoins(User creator, int price){
      creator.setCoinBalance(creator.getCoinBalance() - price);
      userRepository.save(creator);
    }


private void tokenExistance(String token) {
  if (this.userRepository.findUserByToken(token) == null) {
    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You don't have a valid token.");
  }
}

private Boolean tokenValidation(User user, String tokenProvidedbyClient, String errorMessage) {
  if (!tokenProvidedbyClient.equals(user.getToken())) {
    throw new ResponseStatusException(HttpStatus.FORBIDDEN, errorMessage);
  }
  return true;
}

}
