package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Rating;
import ch.uzh.ifi.hase.soprafs24.repository.RatingRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RatingGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RatingPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RatingPutDTO;
import ch.uzh.ifi.hase.soprafs24.service.RatingService;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(RatingController.class)
public class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingService ratingService;

    @MockBean
    private RatingRepository ratingRepository;

    @Test
    public void createRating_validInputRatingCreated() throws Exception {
        Rating rating = new Rating();
        RatingPostDTO ratingPostDTO = new RatingPostDTO();
        Long reviewedId = 1L;
        String token = "111";

        Mockito.when(ratingService.createReview(reviewedId, ratingPostDTO, token)).thenReturn(rating);

        MockHttpServletRequestBuilder postRequest = post("/ratings/{reviewedId}", reviewedId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(asJsonString(ratingPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isCreated());

    }

    @Test
    public void getRatingsOfAnUser_validUserId_ratingsReturned() throws Exception {
        Rating rating = new Rating();
        List<Rating> ratingGetDTOList = Arrays.asList(rating);

        when(ratingService.getRatingsOfAnUser(anyLong(), anyString())).thenReturn(ratingGetDTOList);

        mockMvc.perform(get("/ratings/{userId}", 1L)
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk());
    }

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }

    @Test
    public void deleteRating_validReviewIdAndToken_ratingDeleted() throws Exception {
    long reviewId = 1L;
    String token = "Bearer token";
    RatingPutDTO ratingPutDTO = new RatingPutDTO(); // create a RatingPutDTO object

    doNothing().when(ratingService).deleteReview(reviewId, token);

    mockMvc.perform(delete("/ratings/{reviewId}", reviewId)
                    .header("Authorization", token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(ratingPutDTO))) // include the RatingPutDTO object in the request body
            .andExpect(status().isNoContent());
}


}
