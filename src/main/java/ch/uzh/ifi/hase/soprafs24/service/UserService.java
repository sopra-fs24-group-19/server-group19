package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Rating;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import java.util.*;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  private final RatingService ratingService;

  @Autowired
  public UserService(@Qualifier("userRepository") UserRepository userRepository, @Lazy RatingService ratingService) {
    this.userRepository = userRepository;
    this.ratingService = ratingService;
  }

  public User createUser(User newUser) {
    newUser.setToken(UUID.randomUUID().toString());
    newUser.setCoinBalance(50);
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
          "The username you provided was not found in the database.");
    }
    String userLoginPassword = userLogin.getPassword();
    String userRetrievedPassword = userRetrieved.getPassword();
    if (!userLoginPassword.equals(userRetrievedPassword)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password was not correct.");
    }
    userRetrieved.setToken(UUID.randomUUID().toString());
    this.userRepository.saveAndFlush(userRetrieved);
    return userRetrieved;
  }

  public void editProfile(User userWithPendingChanges, String tokenProvidedbyClient, Long id) {
    tokenExistance(tokenProvidedbyClient);
    User userToEdit = this.userRepository.findUserById(id);
    if (userToEdit == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          String.format("The user with userId: %d was not found.", id));
    }
    tokenValidity(tokenProvidedbyClient, userToEdit.getId());
    userToEdit.setName(userWithPendingChanges.getName());
    userToEdit.setAddress(userWithPendingChanges.getAddress());
    userToEdit.setLatitude(userWithPendingChanges.getLatitude());
    userToEdit.setLongitude(userWithPendingChanges.getLongitude());
    userToEdit.setPhoneNumber(userWithPendingChanges.getPhoneNumber());
    userToEdit.setRadius(userWithPendingChanges.getRadius());
    this.userRepository.saveAndFlush(userToEdit);
  }

  private void checkIfUserExists(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
    String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
    if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "username", "is"));
    }
  }

  public User getUserByToken(String token) {
    return this.userRepository.findByToken(token);
  }

  public User getUserById(long id) {
    Optional<User> user = this.userRepository.findById(id);
    if (user.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no user exists with id" + id);
    }
    return user.get();
  }

  public User getUserWithRatings(long id) {
    Optional<User> user = this.userRepository.findById(id);
    if (user.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no user exists with id" + id);
    }
    User userToEnrich = user.get();

    List<Rating> allRatings = this.ratingService.findRatingsByReviewedId(id);
    int sumOfAllRatings = 0;
    for (Rating rating : allRatings) {
      sumOfAllRatings += rating.getRating();
    }
    int totalRatings = allRatings.size();
    float averageRating = totalRatings > 0 ? (float) sumOfAllRatings / totalRatings : 0;
    userToEnrich.setTotalComments(totalRatings);
    userToEnrich.setAverageStars(averageRating);
    return user.get();
  }

  public long getUserIdByToken(String token) {
    User user = this.userRepository.findByToken(token);
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "\n User not found with token: \n " + token);
    }
    return user.getId();
  }

  public List<User> getCandidatesByTaskId(long taskId) {
    return userRepository.findUsersByTaskId(taskId);
  }

  public void subtractCoins(User creator, int price) {
    creator.setCoinBalance(creator.getCoinBalance() - price);
    userRepository.save(creator);
  }

  public void addCoins(User user, int amount) {
    user.setCoinBalance(user.getCoinBalance() + amount);
    userRepository.save(user);
  }

  public void tokenExistance(String token) {
    if (this.userRepository.findUserByToken(token) == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You don't have a valid token.");
    }
  }

  public void saveUser(User user) {
    userRepository.save(user);
  }

  public List<Object[]> getRankedUsers() {
    List<Object[]> leaderboardData = userRepository.findUsersWithMostTasksAsHelper();
    List<Object[]> rankedUsers = new ArrayList<>();
    int rank = 1;
    int nextRank = 1;
    Long previousTaskCount = null;

    for (int i = 0; i < leaderboardData.size(); i++) {
      long userId = (Long) leaderboardData.get(i)[0];
      User user = getUserById(userId);
      Long taskCount = (Long) leaderboardData.get(i)[1];

      if (previousTaskCount != null && !taskCount.equals(previousTaskCount)) {
        nextRank++;
        rank = nextRank;

      }
      rankedUsers.add(new Object[] { user, taskCount, rank });
      previousTaskCount = taskCount;
    }

    return rankedUsers;
  }

  public boolean tokenValidity(String token, long userId){
    User userRetrieved = this.userRepository.findUserById(userId);
    if (userRetrieved == null) {
      return false;
    }
    String tokenUserRetrieved = userRetrieved.getToken();
    boolean areTokensEqual = tokenUserRetrieved.equals(token);
    return areTokensEqual;
  }

}