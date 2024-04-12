package ch.uzh.ifi.hase.soprafs24.rest.dto;
import ch.uzh.ifi.hase.soprafs24.constant.TaskStatus;
import java.util.Date;

public class TaskGetDTO {
    private String description;

    private TaskStatus status;

    private int compensation;

    private Date date;

    private String address;

    private int duration;

    private String title;
    private long creatorId;

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public TaskStatus getStatus() { return status; }

    public void setStatus(TaskStatus status) { this.status = status; }

    public int getCompensation() { return compensation; }

    public void setCompensation(int compensation) { this.compensation = compensation; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    public int getDuration() { return duration; }

    public void setDuration(int duration) { this.duration = duration; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public long getCreatorId() { return creatorId; }

    public void setCreatorId(long creatorId) { this.creatorId = creatorId; }

}
