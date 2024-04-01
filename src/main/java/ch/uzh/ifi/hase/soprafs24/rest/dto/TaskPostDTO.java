package ch.uzh.ifi.hase.soprafs24.rest.dto;
import java.util.Date;

public class TaskPostDTO {
    // TODO: find a solution for passing date from frontend
    private String description;

    private String title;

    private int compensation;

    private Date date;

    private String address;

    private long creatorId;

    private int duration;

    public long getCreatorId() { return creatorId; }

    public void setCreatorId(long creatorId) { this.creatorId = creatorId; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public int getCompensation() { return compensation; }

    public void setCompensation(int compensation) { this.compensation = compensation; }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public int getDuration() { return duration; }

    public void setDuration(int duration) { this.duration = duration; }


}
