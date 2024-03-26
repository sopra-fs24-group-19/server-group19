package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import ch.uzh.ifi.hase.soprafs24.constant.TaskStatus;

//TODO: add missing columns and figure out good way of joining tables

@Entity
@Table(name = "TASK")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int price;

    @Enumerated // This tells JPA to store the enum values as strings
    private TaskStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public int getPrice() { return price; }

    public void setPrice(int price) { this.price = price; }

    public TaskStatus getStatus() { return status; }

    public void setStatus(TaskStatus status) { this.status = status; }




}