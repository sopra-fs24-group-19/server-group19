package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository("taskRepository")
public interface TaskRepository extends JpaRepository<Task, Long> {
   List<Task> findByCreatorId(long id);
   List<Task> findByHelperId(long id);

   Task findById(long id);

    @Query("SELECT t FROM Task t JOIN t.candidates u WHERE u.id = :userId")
    List<Task> findTasksByApplicantId(long userId);
}
