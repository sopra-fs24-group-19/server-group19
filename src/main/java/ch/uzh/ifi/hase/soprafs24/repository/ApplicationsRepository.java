package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Application;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("applicationsRepository")
public interface ApplicationsRepository extends JpaRepository<Application, Long> {
    Application findByUserAndTask(User user, Task task);
}