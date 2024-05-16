package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Rating;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.RatingService;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RatingController {
    private final RatingService ratingService;

    RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/ratings/{reviewedId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public RatingGetDTO createRating(@PathVariable("reviewedId") long reviewedId, @RequestBody RatingPostDTO ratingPostDTO,
            @RequestHeader("Authorization") String token) {
        Rating newReview = ratingService.createReview(reviewedId, ratingPostDTO, token);
        RatingGetDTO reviewToSendBack = DTOMapper.INSTANCE.convertEntityToRatingGetDTO(newReview);
        return reviewToSendBack;
    }

    @GetMapping("/ratings/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<RatingGetDTO> getRatingsOfAnUser(@PathVariable("userId") long userId, @RequestHeader("Authorization") String token) {
        List <Rating> ratingsList = ratingService.getRatingsOfAnUser(userId, token);
        
        List<RatingGetDTO> ratingGetDTOList = new ArrayList<>();
        for (Rating rating : ratingsList) {
            ratingGetDTOList.add(DTOMapper.INSTANCE.convertEntityToRatingGetDTO(rating));
        }
        return ratingGetDTOList;
    }

    @GetMapping("/ratings/{taskId}/{userId}/isReviewed")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Boolean> checkIfReviewed(@PathVariable("taskId") long taskId, @PathVariable("userId") long userId, @RequestHeader("Authorization") String token) {
        boolean isReviewed = ratingService.checkIfReviewed(taskId, userId, token);
        return ResponseEntity.ok(isReviewed);
    }

    @DeleteMapping("/ratings/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteRating(@PathVariable("reviewId") long reviewId, @RequestBody RatingPutDTO ratingPutDTO,
    @RequestHeader("Authorization") String token) {
        ratingService.deleteReview(reviewId, token);
}
}