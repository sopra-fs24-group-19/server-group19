package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.TaskStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  private User testUser;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    // given
    testUser = new User();
    testUser.setId(1L);
    testUser.setName("testName");
    testUser.setUsername("testUsername");

    // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
    Mockito.when(userRepository.findByToken("validToken")).thenReturn(testUser);
    Mockito.when(userRepository.findByToken("invalidToken")).thenReturn(null);
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

        assertThrows(NoSuchElementException.class, () -> userService.getUserIdByToken(invalidToken),
                "User not found with token: " + invalidToken);
        Mockito.verify(userRepository, Mockito.times(1)).findByToken(invalidToken);
    }



}
