package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class TodoPostDTO {

    private String description;

    private long taskId;
    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public long getTaskId() { return taskId; }

    public void setTaskId(long taskId) { this.taskId = taskId; }

}
