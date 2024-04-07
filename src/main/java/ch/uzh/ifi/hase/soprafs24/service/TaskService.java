package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Task;
//import ch.uzh.ifi.hase.soprafs24.service.UserService;
import ch.uzh.ifi.hase.soprafs24.constant.TaskStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@Transactional
public class TaskService {

    private final Logger log = LoggerFactory.getLogger(TaskService.class);
    private final UserService userService;
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(@Qualifier("taskRepository") TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public Task createTask(Task newTask, long userId) {
        User creator = userService.getUserById(userId);
        boolean valid = checkIfCreatorHasEnoughTokens(creator, newTask);
        if (!valid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "creator does not have enough credits");
        }
        newTask.setCreator(creator);
        newTask.setStatus(TaskStatus.CREATED);
        newTask = taskRepository.save(newTask);
        taskRepository.flush();
        userService.subtractCoins(creator, newTask.getPrice());
        log.debug("Created task: {}", newTask);
        return newTask;
    }

    private boolean checkIfCreatorHasEnoughTokens(User creator, Task task){
        return creator.getCoinBalance() >= task.getPrice();
    }
}
