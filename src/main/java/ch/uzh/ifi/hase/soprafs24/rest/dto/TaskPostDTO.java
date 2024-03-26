package ch.uzh.ifi.hase.soprafs24.rest.dto;
import java.util.Date;

public class TaskPostDTO {
    // TODO: find a solution for passing date from frontend
    private String description;

    private int compensation;

    private Date date;

    private String address;

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public int getCompensation() { return compensation; }

    public void setCompensation(int compensation) { this.compensation = compensation; }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

}
