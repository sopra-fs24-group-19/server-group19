package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

//TODO: add missing columns and figure out good way of joining tables

@Entity
@Table(name = "USER")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private int coinBalance;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private String phoneNumber;

    @Column(nullable = true)
    private float radius;

    @OneToMany(mappedBy = "user")
    private List<Rating> ratings;

    @OneToMany(mappedBy = "task")
    private List<Task> applications;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Rating> getRatings() { return ratings; }

    public void setRatings(List<Rating> ratings) { this.ratings = ratings; }

    public int getCoinBalance() { return coinBalance; }

    public void setCoinBalance(int coinBalance) { this.coinBalance = coinBalance; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public float getRadius() { return radius; }

    public void setRadius(float radius) { this.radius = radius; }

    public List<Task> getApplications() { return applications; }

    public void setApplications(List<Task> applications) { this.applications = applications; }




}
