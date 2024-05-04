package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.repository.RatingRepository;
import ch.uzh.ifi.hase.soprafs24.entity.Rating;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RatingPostDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class RatingService {
    private final Logger log = LoggerFactory.getLogger(RatingService.class);
    private final RatingRepository ratingRepository;
    private final UserService userService;

    @Autowired
    public RatingService(@Qualifier("ratingRepository") RatingRepository ratingRepository, UserService userService) {
        this.ratingRepository = ratingRepository;
        this.userService = userService;
    }

    public Rating createReview(long reviewedId, RatingPostDTO rating, String token) {
        Rating newReview = new Rating();
        newReview.setRating(rating.getStars());
        newReview.setReview(rating.getComment());
        newReview.setReviewed(this.userService.getUserById(reviewedId));
        newReview.setReviewer(this.userService.getUserById(rating.getReviewerId()));
        newReview.setCreationDate(LocalDateTime.now());
        ratingRepository.saveAndFlush(newReview);
        return newReview;
    }

    public void deleteReview(long reviewId, String token) {
        long idUserRetrieved = this.userService.getUserIdByToken(token);
        long idReviewer = this.ratingRepository.findRatingById(reviewId).getReviewer().getId();
        if (idUserRetrieved!=idReviewer){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the author of a review can delete it");
        }
        this.ratingRepository.deleteRatingById(reviewId);
    }

    public List<Rating> getRatingsOfAnUser(Long userId, String token){
        if(token.isEmpty()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You don't have a valid token");
        }
        List<Rating> reviewList = this.ratingRepository.findRatingsByReviewedId(userId);
        return reviewList;
    } 
    // move those in the right service or use the methods that are already there
    /*
    public int findReviews(User reviewer, User reviewed) {
        int reviewsCount = 0;
        for (Rating rating : reviewed.getRatings()) {
            if (rating.getReviewer() == reviewer) {
                reviewsCount += 1;
            }
        }
        return reviewsCount;
    }

    public int findCreatedJobs(User helper, User creator) {
        List<Task> createdJobs = this.taskRepository.findByCreatorId(creator.getId());
        int createdJobsCount = createdJobs.size();
        return createdJobsCount;
    }

    public int findHelpedJobs(User helper, User creator) {
        List<Task> helpedJobs = this.taskRepository.findByHelperId(creator.getId());
        int createdJobsCount = helpedJobs.size();
        return createdJobsCount;
    }
     */
}