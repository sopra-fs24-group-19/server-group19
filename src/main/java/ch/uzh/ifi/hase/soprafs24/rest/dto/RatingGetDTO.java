package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.Date;

public class RatingGetDTO {

    private long id;
    private int stars;
    private String comment;
    private ReviewUserDTO reviewer;
    private ReviewUserDTO reviewed;
    private Date creationDate;

    public void setId(long id) { this.id = id; }

    public long getId() { return id; }

    public void setStars(int stars) { this.stars = stars; }

    public int getStars() { return stars; }

    public String getComment() { return comment; }

    public void setComment(String comment) { this.comment = comment; }

    public ReviewUserDTO getReviewer() { return reviewer; }

    public void setReviewer(ReviewUserDTO reviewer) { this.reviewer = reviewer; }

    public ReviewUserDTO getReviewed() { return reviewed; }

    public void setReviewed(ReviewUserDTO reviewed) { this.reviewed = reviewed; }

    public Date getCreationDate() { return creationDate; }

    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }
}