package ch.uzh.ifi.hase.soprafs24.rest.dto;
public class RatingGetDTO {

    private int stars;
    private String comment;
    private ReviewUserDTO reviewer;
    private ReviewUserDTO reviewed;

    public void setStars(int stars) { this.stars = stars; }

    public int getStars() { return stars; }

    public String getComment() { return comment; }

    public void setComment(String comment) { this.comment = comment; }

    public ReviewUserDTO getReviewer() { return reviewer; }

    public void setReviewer(ReviewUserDTO reviewer) { this.reviewer = reviewer; }

    public ReviewUserDTO getReviewed() { return reviewed; }

    public void setReviewed(ReviewUserDTO reviewed) { this.reviewed = reviewed; }
}