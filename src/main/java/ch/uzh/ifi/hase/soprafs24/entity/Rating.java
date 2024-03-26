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

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

}