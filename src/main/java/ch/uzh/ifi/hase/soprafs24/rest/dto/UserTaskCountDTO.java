package ch.uzh.ifi.hase.soprafs24.rest.dto;
import ch.uzh.ifi.hase.soprafs24.entity.User;

public class UserTaskCountDTO {
    private Long id;
    private String username;
    private Long taskCount;
    private Integer rank;

    public UserTaskCountDTO(Long id, String username, Long taskCount, Integer rank) {
        this.id = id;
        this.username = username;
        this.taskCount = taskCount;
        this.rank = rank;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Long taskCount) {
        this.taskCount = taskCount;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}