package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "TODO")
public class Todo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean done;

    @ManyToOne
    @JoinColumn(name = "authorId", referencedColumnName = "id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "taskId", referencedColumnName = "id")
    private Task task;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public boolean isDone() { return done; }

    public void setDone(boolean done) { this.done = done; }

    public Task getTask() { return task; }

    public void setTask(Task task) { this.task = task; }

    public User getAuthor() { return author; }

    public void setAuthor(User author) { this.author = author; }


}