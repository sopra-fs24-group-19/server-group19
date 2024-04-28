package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class TodoPutDTO {

    private String description;

    private boolean done;

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public boolean isDone() { return done; }

    public void setDone(boolean done) { this.done = done; }

}
