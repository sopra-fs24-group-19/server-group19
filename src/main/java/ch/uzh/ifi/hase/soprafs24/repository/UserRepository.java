package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;


@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
  User findByName(String name);

  User findByUsername(String username);

  User findByToken(String token);
  @Query("SELECT u FROM User u JOIN u.applications t WHERE t.id = :taskId")
  List<User> findUsersByTaskId(Long taskId);

    //@Query("SELECT u FROM User u JOIN u.applications t WHERE t.id = :taskId")
    //List<User> findUsersByTaskId(Long taskId);


}
