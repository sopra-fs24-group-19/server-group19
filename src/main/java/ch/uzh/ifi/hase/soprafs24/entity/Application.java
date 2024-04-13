package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "APPLICATIONS", uniqueConstraints = @UniqueConstraint(columnNames={"taskId", "userId"}))
public class Application implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "taskId", referencedColumnName = "id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    public Task getTask() { return task; }

    public void setTask(Task task) { this.task = task; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

}