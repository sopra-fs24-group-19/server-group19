package ch.uzh.ifi.hase.soprafs24.service;
import java.util.Arrays;
import java.time.LocalDateTime;

import ch.uzh.ifi.hase.soprafs24.constant.TaskStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Rating;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.RatingRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private RatingRepository ratingRepository;
  @Mock
  private RatingService ratingService;
  @Mock
  private TaskService taskService;

  @InjectMocks
  private UserService userService;


  private User testUser, testUser2;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    ratingService = Mockito.mock(RatingService.class);

    testUser = new User();
    testUser.setId(1L);
    testUser.setName("testName");
    testUser.setUsername("testUsername");
    testUser.setToken("validToken");

    testUser2 = new User();
    testUser2.setId(2L);
    testUser2.setName("testName2");
    testUser2.setUsername("testUsername2");

    when(userRepository.save(Mockito.any())).thenReturn(testUser);
    when(userRepository.findByToken("validToken")).thenReturn(testUser);
    when(userRepository.findByToken("invalidToken")).thenReturn(null);


    when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
    when(userRepository.findById(2L)).thenReturn(Optional.of(testUser2));
  }

    @Test
    public void getUserIdByToken_success() {

        String token = "validToken";

        long userId = userService.getUserIdByToken(token);

        assertEquals(testUser.getId(), userId);
        Mockito.verify(userRepository, Mockito.times(1)).findByToken(token);
    }

    @Test
    public void getUserIdByToken_invalidToken_throwsException() {

        String invalidToken = "invalidToken";

        assertThrows(ResponseStatusException.class, () -> userService.getUserIdByToken(invalidToken),
                "User not found with token: " + invalidToken);
        Mockito.verify(userRepository, Mockito.times(1)).findByToken(invalidToken);
    }

    @Test
    public void subtractCoins_successfulSubtraction() {
        testUser.setCoinBalance(100);

        userService.subtractCoins(testUser, 50);

        assertEquals(50, testUser.getCoinBalance());
        Mockito.verify(userRepository).save(testUser);
    }

    @Test
    public void addCoins_successfulAddition() {

        testUser.setCoinBalance(20);

        userService.addCoins(testUser, 50);

        assertEquals(70, testUser.getCoinBalance());
        Mockito.verify(userRepository).save(testUser);
    }

    @Test
    public void getRankedUsers_successWithMultipleUsers() {
        List<Object[]> testData = new ArrayList<>();
        testData.add(new Object[]{1L, 5L});
        testData.add(new Object[]{2L, 3L});

        when(userRepository.findUsersWithMostTasksAsHelper()).thenReturn(testData);

        List<Object[]> rankedUsers = userService.getRankedUsers();

        assertEquals(2, rankedUsers.size());
        assertEquals(testUser, rankedUsers.get(0)[0]);
        assertEquals(5L, rankedUsers.get(0)[1]);
        assertEquals(1, rankedUsers.get(0)[2]);
        assertEquals(testUser2, rankedUsers.get(1)[0]);
        assertEquals(3L, rankedUsers.get(1)[1]);
        assertEquals(2, rankedUsers.get(1)[2]);

        Mockito.verify(userRepository).findUsersWithMostTasksAsHelper();
    }

    @Test
    public void getRankedUsers_successWithNoUsers() {

        List<Object[]> testData = new ArrayList<>();
        when(userRepository.findUsersWithMostTasksAsHelper()).thenReturn(testData);

        List<Object[]> rankedUsers = userService.getRankedUsers();

        assertTrue(rankedUsers.isEmpty());
        Mockito.verify(userRepository).findUsersWithMostTasksAsHelper();
    }

    @Test
    public void login_success() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("correctPassword");
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        User result = userService.login(user);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).saveAndFlush(user);
    }

    @Test
    public void login_failure_usernameNotFound() {
        User user = new User();
        user.setUsername("nonExistentUser");
        user.setPassword("anyPassword");
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> userService.login(user),
                "The username you provided was not found in the database.");
    }

    @Test
    public void login_failure_incorrectPassword() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("wrongPassword");
        User storedUser = new User();
        storedUser.setUsername("testUser");
        storedUser.setPassword("correctPassword");
        when(userRepository.findByUsername("testUser")).thenReturn(storedUser);

        assertThrows(ResponseStatusException.class, () -> userService.login(user),
                "The password was not correct.");
    }

    @Test
    public void editProfile_success() {
        User originalUser = new User();
        originalUser.setId(1L);
        originalUser.setToken("validToken");
        User updatedUser = new User();
        updatedUser.setName("New Name");

        when(userRepository.findUserById(1L)).thenReturn(originalUser);
        when(userRepository.saveAndFlush(Mockito.any(User.class))).thenReturn(updatedUser);
        when(userRepository.findUserByToken("validToken")).thenReturn(updatedUser);

        userService.editProfile(updatedUser, "validToken", 1L);

        Mockito.verify(userRepository).saveAndFlush(originalUser);
        assertEquals("New Name", originalUser.getName());
    }

    @Test
    public void editProfile_invalidToken_throwsException() {
        User user = new User();
        user.setId(1L);
        user.setToken("otherToken");
        when(userRepository.findUserById(1L)).thenReturn(user);

        assertThrows(ResponseStatusException.class, () -> userService.editProfile(new User(), "invalidToken", 1L),
                "Token is invalid.");
    }

    @Test
    public void getUserByToken_success() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findByToken("validToken")).thenReturn(user);

        User result = userService.getUserByToken("validToken");

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void getUserWithRatings_noUserFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.getUserWithRatings(1L),
                "no user exists with id1");
    }

    @Test
    public void tokenValidity_returnsFalse_whenTokenIsInvalid() {
        boolean result = userService.tokenValidity("invalidToken", 1L);
        assertFalse(result);
    }

    @Test
    public void tokenValidity_returnsFalse_whenUserIdDoesNotMatch() {
        boolean result = userService.tokenValidity("validToken", 2L);
        assertFalse(result);
    }



@Test
public void getUserWithRatings_returnsUserWithNoRatings_whenUserHasNoRatings() {
    User user = new User();
    user.setId(1L);

    List<Rating> ratings = new ArrayList<>();

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(ratingService.findRatingsByReviewedId(1L)).thenReturn(ratings);

    User result = userService.getUserWithRatings(1L);

    assertEquals(0, result.getTotalComments());
    assertEquals(0, result.getAverageStars());
}

@Test
public void getUserWithRatings_throwsException_whenUserDoesNotExist() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ResponseStatusException.class, () -> userService.getUserWithRatings(1L));
}
}
