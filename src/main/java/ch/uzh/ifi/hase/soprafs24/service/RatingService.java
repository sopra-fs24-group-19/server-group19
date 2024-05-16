package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Task;
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
    private final RatingRepository ratingRepository;
    private final UserService userService;

    private final TaskService taskService;

    @Autowired
    public RatingService(@Qualifier("ratingRepository") RatingRepository ratingRepository, UserService userService, TaskService taskService) {
        this.ratingRepository = ratingRepository;
        this.userService = userService;
        this.taskService = taskService;
    }

    public Rating createReview(long reviewedId, RatingPostDTO ratingPostDTO, String token) {
        long userIdFromToken = userService.getUserIdByToken(token);
        long reviewCreatorId = ratingPostDTO.getReviewerId();

        if (userIdFromToken != reviewCreatorId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "The token does not belong to the creator of the review");
        }

        long taskId = ratingPostDTO.getTaskId();
        Task task = taskService.getTaskById(taskId);

        isReviewAuthorized(reviewCreatorId, reviewedId, taskId);

        Rating newReview = new Rating();
        newReview.setRating(ratingPostDTO.getStars());
        newReview.setReview(ratingPostDTO.getComment());
        newReview.setReviewed(this.userService.getUserWithRatings(reviewedId));
        newReview.setReviewer(this.userService.getUserWithRatings(reviewCreatorId));
        newReview.setTask(task);
        newReview.setCreationDate(LocalDateTime.now());
        ratingRepository.saveAndFlush(newReview);
        return newReview;
    }

    public void deleteReview(long reviewId, String token) {
        long idUserRetrieved = this.userService.getUserIdByToken(token);
        long idReviewer = this.ratingRepository.findRatingById(reviewId).getReviewer().getId();
        if (idUserRetrieved != idReviewer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the author of a review can delete it");
        }
        this.ratingRepository.deleteRatingById(reviewId);
    }

    public List<Rating> findRatingsByReviewedId (long id){
        return this.ratingRepository.findRatingsByReviewedId(id);
    }

    public List<Rating> getRatingsOfAnUser(Long userId, String token) {
        if (token.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You don't have a valid token");
        }
        List<Rating> reviewList = this.ratingRepository.findRatingsByReviewedId(userId);
        return reviewList;
    } 

    public boolean isReviewAuthorized(long id_reviewer, long id_reviewed, long id_task) {
        Task task = taskService.getTaskById(id_task);

        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }

        // Check if the reviewer is either the creator or the helper of the task
        if (task.getCreator().getId() != id_reviewer && task.getHelper().getId() != id_reviewer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the creator or the helper of the task can leave a review.");
        }

        // Check if the reviewee is either the helper or the creator of the task, but
        // not the same as the reviewer
        if ((task.getHelper().getId() != id_reviewed && task.getCreator().getId() != id_reviewed)
                || id_reviewer == id_reviewed) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, 
                    "The reviewed user must be either the helper or the creator of the task, and cannot be the same as the reviewer.");
        }

        // Check if a review has already been left for this task by the reviewer
        if (ratingRepository.existsByTaskAndReviewerId(task, id_reviewer)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only one review can be left for a given task by the same reviewer.");
        }

        return true;
    }

    public boolean checkIfReviewed(long taskId, long userId, String token) {
        Task task = taskService.getTaskById(taskId);
        return ratingRepository.existsByTaskAndReviewerId(task, userId);
    }
}