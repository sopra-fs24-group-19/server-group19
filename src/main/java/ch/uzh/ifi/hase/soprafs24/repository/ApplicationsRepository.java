package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Application;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

@Repository("applicationsRepository")
public interface ApplicationsRepository extends JpaRepository<Application, Long> {
    Application findByUserAndTask(User user, Task task);

    @Transactional
    @Modifying
    @Query("DELETE FROM Application a WHERE a.task.id = ?1")
    void deleteByTaskId(Long taskId);
}