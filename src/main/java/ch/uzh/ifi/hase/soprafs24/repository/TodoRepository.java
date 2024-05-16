package ch.uzh.ifi.hase.soprafs24.repository;
import ch.uzh.ifi.hase.soprafs24.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("todoRepository")
public interface TodoRepository extends JpaRepository<Todo, Long> {

    Todo findTodoById(Long id);

    List<Todo> findByTaskId(Long taskId);

    List<Todo> findAllByTaskId(Long taskId);
}
