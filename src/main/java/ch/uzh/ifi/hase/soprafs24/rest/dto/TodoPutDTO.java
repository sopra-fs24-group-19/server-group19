package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class TodoPutDTO {

    private long id;

    private String description;

    private boolean done;

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public boolean isDone() { return done; }

    public void setDone(boolean done) { this.done = done; }

}
