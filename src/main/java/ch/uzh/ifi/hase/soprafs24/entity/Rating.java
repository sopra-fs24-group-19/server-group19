package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "RATING")
public class Rating implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private String review;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name="reviewer_id")
    private User reviewer;

    @ManyToOne
    @JoinColumn(name="reviewed_id")
    @JsonBackReference
    private User reviewed;

    @ManyToOne
    @JoinColumn(name="task_id")
    private Task task;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getReviewer() { return reviewer; }

    public void setReviewer(User reviewer) { this.reviewer = reviewer; }

    public User getReviewed() { return reviewed; }

    public void setReviewed(User reviewed) { this.reviewed = reviewed; }

    public int getRating() { return rating; }

    public void setRating(int rating) { this.rating = rating; }

    public String getReview() { return review; }

    public void setReview(String review) { this.review = review; }

    public LocalDateTime getCreationDate() { return creationDate; }

    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

}