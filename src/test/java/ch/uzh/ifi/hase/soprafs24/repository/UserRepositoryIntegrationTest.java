package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.constant.TaskStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

@DataJpaTest
public class UserRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void findByName_success() {
    // given
    User user = new User();
    user.setName("Firstname Lastname");
    user.setUsername("firstname@lastname");
    user.setToken("1");
    user.setPassword("password");

    entityManager.persist(user);
    entityManager.flush();

    // when
    User found = userRepository.findByName(user.getName());

    // then
    assertNotNull(found.getId());
    assertEquals(found.getName(), user.getName());
    assertEquals(found.getUsername(), user.getUsername());
    assertEquals(found.getToken(), user.getToken());
  }
    @Test
    public void findByUsername_success() {
        // given
        User user = new User();
        user.setName("Secondname Lastname");
        user.setUsername("secondname@lastname");
        user.setToken("2");
        user.setPassword("password");

        entityManager.persist(user);
        entityManager.flush();

        // when
        User found = userRepository.findByUsername(user.getUsername());

        // then
        assertNotNull(found);
        assertEquals(user.getUsername(), found.getUsername());
    }

    @Test
    public void findByToken_success() {

        User user = new User();
        user.setName("Thirdname Lastname");
        user.setUsername("thirdname@lastname");
        user.setToken("3");
        user.setPassword("password");

        entityManager.persist(user);
        entityManager.flush();

        User found = userRepository.findByToken(user.getToken());

        assertNotNull(found);
        assertEquals(user.getToken(), found.getToken());
    }

    //TODO: fix this test
    //@Test
    public void findUsersByTaskId_success() {
        // given
        User user = new User();
        user.setName("Fourthname Lastname");
        user.setUsername("fourthname@lastname");
        user.setToken("4");
        user.setPassword("password");

        Task task = new Task();
        task.setId(1L);
        user.setApplications(List.of(task));

        entityManager.persist(user);
        entityManager.persist(task);
        entityManager.flush();

        List<User> users = userRepository.findUsersByTaskId(task.getId());

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals(user.getUsername(), users.get(0).getUsername());
    }
}

