package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class TaskPutDTO {
    //this DTO is prob not needed at all
    private long taskId;

    private long userId;

    private long helperId;

    public long getTaskId() { return taskId; }

    public void setTaskId(long taskId) { this.taskId = taskId; }

    public long getUserId() { return userId; }

    public void setUserId(long userId) { this.userId = userId; }

    public long getHelperId() { return helperId; }

    public void setHelperId(long helperId) { this.helperId = helperId; }

}
