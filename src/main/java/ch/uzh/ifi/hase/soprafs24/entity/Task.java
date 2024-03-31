package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import ch.uzh.ifi.hase.soprafs24.constant.TaskStatus;
import java.util.List;
import java.util.Date;

@Entity
@Table(name = "TASK")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Date date;

    @Enumerated
    @Column(nullable=false)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "creatorId", referencedColumnName = "id")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "helperId", referencedColumnName = "id")
    private User helper;

    @ManyToMany(mappedBy = "applications")
    private List<User> candidates;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public int getPrice() { return price; }

    public void setPrice(int price) { this.price = price; }

    public TaskStatus getStatus() { return status; }

    public void setStatus(TaskStatus status) { this.status = status; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    public User getCreator() { return creator; }

    public void setCreator(User creator) { this.creator = creator; }

    public User getHelper() { return helper; }

    public void setHelper(User helper) { this.helper = helper; }

    public List<User> getCandidates() { return candidates; }

    public void setCandidates(List<User> candidates) { this.candidates = candidates; }

}