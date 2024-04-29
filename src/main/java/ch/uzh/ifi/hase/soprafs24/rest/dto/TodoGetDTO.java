package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class TodoGetDTO {

    private String description;

    private boolean done;

    private long authorId;

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public boolean isDone() { return done; }

    public void setDone(boolean done) { this.done = done; }

    public long getAuthorId() { return authorId; }

    public void setAuthorId(long author) { this.authorId = author; }


}
