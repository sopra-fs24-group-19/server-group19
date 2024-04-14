package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.repository.RatingRepository;
import ch.uzh.ifi.hase.soprafs24.repository.TaskRepository;
import ch.uzh.ifi.hase.soprafs24.entity.Rating;
import ch.uzh.ifi.hase.soprafs24.entity.Task;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RatingPostDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException.Forbidden;
import org.springframework.web.server.ResponseStatusException;
import java.util.NoSuchElementException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Service
@Transactional
public class RatingService {
    private final Logger log = LoggerFactory.getLogger(RatingService.class);
    private final RatingRepository ratingRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public RatingService(@Qualifier("ratingRepository") RatingRepository ratingRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
  }


    public void createReview(RatingPostDTO rating, String token){
        Rating newReview = new Rating();
        newReview.setRating(rating.getStars());
        newReview.setReview(rating.getComment());
        newReview.setReviewed(this.userRepository.findUserById(rating.getReviewedId()));
        newReview.setReviewer(this.userRepository.findUserById(rating.getReviewerId()));
        ratingRepository.saveAndFlush(newReview);
  }

    public int findReviews(User reviewer, User reviewed){
        int reviewsCount = 0;
        for (Rating rating : reviewed.getRatings()){
            if (rating.getReviewer() == reviewer){
                reviewsCount += 1;
            }
        }
        return reviewsCount;
    }

    public int findCreatedJobs(User helper, User creator){
        List<Task> createdJobs = this.taskRepository.findByCreatorId(creator.getId());
        int createdJobsCount = createdJobs.size();
        return createdJobsCount;
    }

    public int findHelpedJobs(User helper, User creator){
        List<Task> helpedJobs = this.taskRepository.findByHelperId(creator.getId());
        int createdJobsCount = helpedJobs.size();

        return createdJobsCount;
    }
}