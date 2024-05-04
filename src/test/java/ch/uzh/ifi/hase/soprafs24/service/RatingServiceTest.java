package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Rating;
import ch.uzh.ifi.hase.soprafs24.entity.Task;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.RatingRepository;
import ch.uzh.ifi.hase.soprafs24.repository.TaskRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RatingPostDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;
    @Mock
    private UserService userService;
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RatingService ratingService;

    private User testUser;
    private Rating testRating;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setToken("validToken");

        testRating = new Rating();
        testRating.setId(1L);
        testRating.setReviewer(testUser);
        testRating.setReviewed(testUser);
        testRating.setRating(5);
        testRating.setReview("Great job!");
        testRating.setCreationDate(LocalDateTime.now());
    }
    
    @Test
    public void deleteReview_validInputs_deletesReview() {
        when(userService.getUserIdByToken(anyString())).thenReturn(testUser.getId());
        when(ratingRepository.findRatingById(anyLong())).thenReturn(testRating);

        ratingService.deleteReview(testRating.getId(), testUser.getToken());

        verify(ratingRepository, times(1)).deleteRatingById(testRating.getId());
    }


    @Test
    public void getRatingsOfAnUser_validInputs_returnsReviewList() {
        when(ratingRepository.findRatingsByReviewedId(anyLong())).thenReturn(Arrays.asList(testRating));

        List<Rating> reviewList = ratingService.getRatingsOfAnUser(testUser.getId(), testUser.getToken());

        assertEquals(1, reviewList.size());
        assertEquals(testRating, reviewList.get(0));
    }

    @Test
    public void getRatingsOfAnUser_invalidToken_throwsUnauthorized() {
        assertThrows(ResponseStatusException.class, () -> ratingService.getRatingsOfAnUser(testUser.getId(), ""));
    }

    /*
    @Test
    public void findReviews_validInputs_returnsReviewCount() {
        User reviewed = new User();
        reviewed.setId(2L);
        reviewed.setRatings(Arrays.asList(testRating));

        int reviewsCount = ratingService.findReviews(testUser, reviewed);

        assertEquals(1, reviewsCount);
    }

    @Test
    public void findCreatedJobs_validInputs_returnsJobCount() {
        User creator = new User();
        creator.setId(2L);

        Task task = new Task();
        task.setId(1L);
        task.setCreator(creator);

        when(taskRepository.findByCreatorId(anyLong())).thenReturn(Arrays.asList(task));

        int createdJobsCount = ratingService.findCreatedJobs(testUser, creator);

        assertEquals(1, createdJobsCount);
    }

    @Test
    public void findHelpedJobs_validInputs_returnsJobCount() {
        User helper = new User();
        helper.setId(2L);

        Task task = new Task();
        task.setId(1L);
        task.setHelper(helper);

        when(taskRepository.findByHelperId(anyLong())).thenReturn(Arrays.asList(task));

        int helpedJobsCount = ratingService.findHelpedJobs(testUser, helper);

        assertEquals(1, helpedJobsCount);
    }
    */

}