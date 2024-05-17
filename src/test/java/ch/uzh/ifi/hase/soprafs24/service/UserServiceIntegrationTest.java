package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
public class UserServiceIntegrationTest {

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private User user;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        user = new User();
        user.setUsername("testUser");
        user.setPassword("password123");
        user.setName("Test Name");
    }

    @Test
    public void testCreateUser() {

        User createdUser = userService.createUser(user);
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals("testUser", createdUser.getUsername());
        assertEquals(50, createdUser.getCoinBalance());
    }

    @Test
    public void testLoginSuccess() {

        userService.createUser(user);

        User loginAttempt = new User();
        loginAttempt.setUsername("testUser");
        loginAttempt.setPassword("password123");

        User loggedInUser = userService.login(loginAttempt);
        assertNotNull(loggedInUser.getToken());
    }

    @Test
    public void testLoginFailure() {
        User loginAttempt = new User();
        loginAttempt.setUsername("nonexistent");
        loginAttempt.setPassword("password");

        assertThrows(ResponseStatusException.class, () -> userService.login(loginAttempt));
    }

    @Test
    public void testEditProfile() {
        User createdUser = userService.createUser(user);

        User updateInfo = new User();
        updateInfo.setName("New Name");
        updateInfo.setAddress("New Address");

        userService.editProfile(updateInfo, createdUser.getToken(), createdUser.getId());

        User updatedUser = userService.getUserById(createdUser.getId());
        assertEquals("New Name", updatedUser.getName());
        assertEquals("New Address", updatedUser.getAddress());
    }

    @Test
    public void testEditProfileUnauthorized() {
        User newUser = new User();
        newUser.setUsername("editUserFail");
        newUser.setPassword("password123");
        User createdUser = userService.createUser(user);

        User updateInfo = new User();
        updateInfo.setName("Unauthorized Name");

        assertThrows(ResponseStatusException.class, () -> userService.editProfile(updateInfo, "invalidToken", createdUser.getId()));
    }

}
