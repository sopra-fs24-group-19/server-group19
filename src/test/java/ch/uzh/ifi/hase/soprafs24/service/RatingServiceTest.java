package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.entity.Rating;
import ch.uzh.ifi.hase.soprafs24.entity.Task;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.RatingRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RatingPostDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
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
    private TaskService taskService;

    @InjectMocks
    private RatingService ratingService;

    private User testUser, testHelper;
    private Rating testRating;

    private Task testTask;
    private RatingPostDTO ratingPostDTO;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setToken("validToken");

        testHelper = new User();
        testHelper.setId(2L);
        testHelper.setToken("validToken2");

        testRating = new Rating();
        testRating.setId(1L);
        testRating.setReviewer(testUser);
        testRating.setReviewed(testUser);
        testRating.setRating(5);
        testRating.setReview("Great job!");
        testRating.setCreationDate(LocalDateTime.now());

        testTask = new Task();
        testTask.setId(1L);
        testTask.setCreator(testUser);
        testTask.setHelper(testHelper);

        ratingPostDTO = new RatingPostDTO();
        ratingPostDTO.setReviewerId(1L);
        ratingPostDTO.setStars(5);
        ratingPostDTO.setComment("Great job!");
        ratingPostDTO.setTaskId(1L);
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

    @Test
    public void deleteReview_invalidToken_throwsForbidden() {
        when(userService.getUserIdByToken("invalidToken")).thenReturn(2L);
        when(ratingRepository.findRatingById(anyLong())).thenReturn(testRating);

        assertThrows(ResponseStatusException.class, () -> ratingService.deleteReview(testRating.getId(), "invalidToken"));
    }

    @Test
    public void findRatingsByReviewedId_validInputs_returnsRatings() {
        when(ratingRepository.findRatingsByReviewedId(1L)).thenReturn(Arrays.asList(testRating));

        List<Rating> ratings = ratingService.findRatingsByReviewedId(1L);

        assertNotNull(ratings);
        assertFalse(ratings.isEmpty());
        assertEquals(1, ratings.size());
        assertEquals(testRating, ratings.get(0));
    }

    @Test
    public void createReview_validInputs_createsAndSavesReview() {
        when(userService.getUserIdByToken("validToken")).thenReturn(1L);
        when(userService.getUserWithRatings(anyLong())).thenReturn(testUser);
        when(taskService.getTaskById(anyLong())).thenReturn(testTask);
        when(ratingRepository.saveAndFlush(any(Rating.class))).thenReturn(testRating);

        RatingPostDTO dto = new RatingPostDTO();
        dto.setReviewerId(1L);
        dto.setStars(5);
        dto.setComment("Great job!");
        dto.setTaskId(1L);

        Rating result = ratingService.createReview(2L, dto, "validToken");

        assertNotNull(result);
        assertEquals(5, result.getRating());
        verify(ratingRepository, times(1)).saveAndFlush(any(Rating.class));
    }

    @Test
    public void isReviewAuthorized_validInputs_returnsTrue() {

        when(taskService.getTaskById(anyLong())).thenReturn(testTask);
        when(ratingRepository.existsByTaskAndReviewerId(any(Task.class), anyLong())).thenReturn(false);

        assertTrue(ratingService.isReviewAuthorized(1L, 2L, 1L));
    }

    @Test
    public void checkIfReviewed_alreadyReviewed_returnsTrue() {
        when(taskService.getTaskById(anyLong())).thenReturn(testTask);
        when(ratingRepository.existsByTaskAndReviewerId(any(Task.class), anyLong())).thenReturn(true);

        assertTrue(ratingService.checkIfReviewed(1L, 1L, "validToken"));
    }

    @Test
    public void checkIfReviewed_notReviewed_returnsFalse() {
        when(taskService.getTaskById(anyLong())).thenReturn(testTask);
        when(ratingRepository.existsByTaskAndReviewerId(any(Task.class), anyLong())).thenReturn(false);

        assertFalse(ratingService.checkIfReviewed(1L, 1L, "validToken"));
    }

    @Test
    public void isReviewAuthorized_taskIsNull_throwsForbidden() {
        when(taskService.getTaskById(anyLong())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> ratingService.isReviewAuthorized(3L, testUser.getId(), testTask.getId()));
    }

    @Test
    public void isReviewAuthorized_reviewerIsSameAsReviewed_throwsForbidden() {
        when(taskService.getTaskById(anyLong())).thenReturn(testTask);

        assertThrows(ResponseStatusException.class, () -> ratingService.isReviewAuthorized(testUser.getId(), testUser.getId(), testTask.getId()));
    }

    @Test
    public void isReviewAuthorized_alreadyReviewed_throwsForbidden() {
        when(taskService.getTaskById(anyLong())).thenReturn(testTask);
        when(ratingRepository.existsByTaskAndReviewerId(any(Task.class), anyLong())).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> ratingService.isReviewAuthorized(testUser.getId(), testHelper.getId(), testTask.getId()));
    }


    @Test
    public void createReview_invalidToken_throwsForbidden() {
        when(userService.getUserIdByToken(anyString())).thenReturn(2L);

        assertThrows(ResponseStatusException.class, () -> ratingService.createReview(1L, ratingPostDTO, "invalidToken"));
    }

    @Test
    public void createReview_invalidTaskId_throwsNotFound() {
        when(userService.getUserIdByToken(anyString())).thenReturn(1L);
        when(taskService.getTaskById(anyLong())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> ratingService.createReview(1L, ratingPostDTO, "validToken"));
    }

    @Test
    public void createReview_reviewNotAuthorized_throwsForbidden() {
        when(userService.getUserIdByToken(anyString())).thenReturn(1L);
        when(taskService.getTaskById(anyLong())).thenReturn(testTask);
        when(ratingRepository.existsByTaskAndReviewerId(any(Task.class), anyLong())).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> ratingService.createReview(1L, ratingPostDTO, "validToken"));
    }

}