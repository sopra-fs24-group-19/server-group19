package ch.uzh.ifi.hase.soprafs24.rest.dto;
import ch.uzh.ifi.hase.soprafs24.constant.TaskStatus;

import java.util.Date;

public class TaskGetDTO {
    private long id;

    private String description;

    private TaskStatus status;

    private int compensation;

    private Date date;

    private String address;

    private String latitude;
    
    private String longitude;

    private int duration;

    private String title;

    private long creatorId;

    private long helperId;

    //private User creator;

    //private User helper;

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public long getHelperId() { return helperId; }

    public void setHelperId(long helperId) { this.helperId = helperId; }

    public TaskStatus getStatus() { return status; }

    public void setStatus(TaskStatus status) { this.status = status; }

    public int getCompensation() { return compensation; }

    public void setCompensation(int compensation) { this.compensation = compensation; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }
    
    public String getLatitude() { return latitude; }

    public void setLatitude(String latitude) { this.latitude = latitude; }

    public String getLongitude() { return longitude; }

    public void setLongitude(String longitude) { this.longitude = longitude; }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    public int getDuration() { return duration; }

    public void setDuration(int duration) { this.duration = duration; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public long getCreatorId() { return creatorId; }

    public void setCreatorId(long creatorId) { this.creatorId = creatorId; }
    
    //public User getCreator() { return creator; }

    //public void setCreator(User creator) { this.creator = creator; }
    
    //public User getHelper() { return helper; }

    //public void setHelper(User helper) { this.helper = helper; }
}