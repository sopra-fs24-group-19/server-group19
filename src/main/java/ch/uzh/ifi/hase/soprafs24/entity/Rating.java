package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;


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

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public int getRating() { return rating; }

    public void setRating(int rating) { this.rating = rating; }

    public String getReview() { return review; }

    public void setReview(String review) { this.review = review; }

}