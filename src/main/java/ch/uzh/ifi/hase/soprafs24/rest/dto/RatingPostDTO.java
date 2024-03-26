package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class RatingPostDTO {

    private int stars;

    private String comment;

    public void setStars(int stars) { this.stars = stars; }

    public int getStars() { return stars; }

    public String getComment() { return comment; }

    public void setComment(String comment) { this.comment = comment; }



}
