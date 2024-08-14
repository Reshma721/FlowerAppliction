package com.floral.floralfiessy.controller;

import com.floral.floralfiessy.dto.ReviewDto;
import com.floral.floralfiessy.exception.UserNotFoundException;
import com.floral.floralfiessy.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/getAllReviews")
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        List<ReviewDto> reviewDtos = reviewService.getAllReview();
        return new ResponseEntity<>(reviewDtos, HttpStatus.OK);
    }

    @GetMapping("/getReviewById/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long id) throws UserNotFoundException {
        ReviewDto reviewDto = reviewService.getReviewById(id);
        return new ResponseEntity<>(reviewDto, HttpStatus.OK);
    }

    @PostMapping("/addReview")
    public ResponseEntity<String> addReview(@RequestBody @Valid ReviewDto reviewDto) throws UserNotFoundException {
        reviewService.addReview(reviewDto);
        return ResponseEntity.ok("Review added successfully!");
    }

    @PutMapping("/updateReview/{id}")
    public ResponseEntity<String> updateReview(@PathVariable Long id, @RequestBody @Valid ReviewDto reviewDto) throws UserNotFoundException {
        reviewService.updateReview(id, reviewDto);
        return ResponseEntity.ok("Review updated successfully!");
    }

    @DeleteMapping("/deleteReview/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id) throws UserNotFoundException {
        reviewService.deleteReview(id);
        return ResponseEntity.ok("Review deleted successfully!");
    }
}
