package ch.uzh.ifi.hase.soprafs24.controller;
import java.util.Collections;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TaskController {

    private final TaskService taskService;

    TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void createTask(@RequestBody TaskPostDTO taskPostDTO) {
        Task taskInput = DTOMapper.INSTANCE.convertTaskPostDTOToEntity(taskPostDTO);
        long creatorId = taskPostDTO.getCreatorId();
        Task createdTask = taskService.createTask(taskInput,creatorId);
    }

    @GetMapping("/tasks")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<TaskGetDTO> getAllTasks() {
        // TODO: implement
        return Collections.emptyList();
    }

    @PutMapping("/apply")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void apply(@RequestBody TaskPutDTO taskPutDTO) {
        //TODO: implement
    }

    @GetMapping("/candidates/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getCandidates(@RequestParam("taskId") long id) {
        //TODO: implement
        return Collections.emptyList();
    }

    @PutMapping("/tasks/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void selectCandidate(@RequestBody TaskPutDTO taskPutDTO) {
        //TODO: implement
    }

    @PutMapping("/tasks/{taskId}/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void confirmTask(@RequestBody TaskPutDTO taskPutDTO) {
        //TODO: implement
    }

    @PutMapping("/tasks/{taskId}/refuse")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void refuseTask(@RequestBody TaskPutDTO taskPutDTO) {
        //TODO: implement
    }

    @GetMapping("/tasks/created/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<TaskGetDTO> getMyCreatedTasks(@RequestBody long userId) {
        // TODO: implement
        return Collections.emptyList();
    }

    @GetMapping("/tasks/applied/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<TaskGetDTO> getMyAppliedTasks(@RequestBody long userId) {
        // TODO: implement
        return Collections.emptyList();
    }

}
