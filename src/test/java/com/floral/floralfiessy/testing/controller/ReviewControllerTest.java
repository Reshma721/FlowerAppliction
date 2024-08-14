package com.floral.floralfiessy.testing.controller;

import com.floral.floralfiessy.dto.ReviewDto;
import com.floral.floralfiessy.exception.GlobalExceptionHandler;
import com.floral.floralfiessy.exception.UserNotFoundException;
import com.floral.floralfiessy.service.ReviewService;
import com.floral.floralfiessy.controller.ReviewController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReviewControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ReviewController reviewController;

    @Mock
    private ReviewService reviewService;

    private ReviewDto reviewDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        reviewDto = new ReviewDto();
        reviewDto.setId(1L);
        reviewDto.setUserId(1L);
        reviewDto.setProductId(1L);
        reviewDto.setReview("Great product!");
    }

    @Test
    void getAllReviews() throws Exception {
        List<ReviewDto> reviewList = Arrays.asList(reviewDto);
        when(reviewService.getAllReview()).thenReturn(reviewList);

        mockMvc.perform(get("/reviews/getAllReviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].review").value("Great product!"));
    }

    @Test
    void getAllReviews_NoReviews() throws Exception {
        when(reviewService.getAllReview()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/reviews/getAllReviews"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getReviewById_ReviewExists() throws Exception {
        when(reviewService.getReviewById(1L)).thenReturn(reviewDto);

        mockMvc.perform(get("/reviews/getReviewById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.review").value("Great product!"));
    }

    @Test
    void getReviewById_ReviewDoesNotExist() throws Exception {
        when(reviewService.getReviewById(2L)).thenThrow(new UserNotFoundException("Review not found"));

        mockMvc.perform(get("/reviews/getReviewById/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Review not found"));
    }

    @Test
    void addReview() throws Exception {
        doNothing().when(reviewService).addReview(any(ReviewDto.class));

        mockMvc.perform(post("/reviews/addReview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1, \"productId\":1, \"review\":\"Great product!\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Review added successfully!"));
    }

    @Test
    void updateReview_ReviewExists() throws Exception {
        doNothing().when(reviewService).updateReview(anyLong(), any(ReviewDto.class));

        mockMvc.perform(put("/reviews/updateReview/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1, \"productId\":1, \"review\":\"Updated review!\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Review updated successfully!"));
    }

    @Test
    void updateReview_ReviewDoesNotExist() throws Exception {
        doThrow(new UserNotFoundException("Review not found")).when(reviewService).updateReview(anyLong(), any(ReviewDto.class));

        mockMvc.perform(put("/reviews/updateReview/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1, \"productId\":1, \"review\":\"Updated review!\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteReview_ReviewExists() throws Exception {
        doNothing().when(reviewService).deleteReview(anyLong());

        mockMvc.perform(delete("/reviews/deleteReview/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Review deleted successfully!"));
    }

    @Test
    void deleteReview_ReviewDoesNotExist() throws Exception {
        doThrow(new UserNotFoundException("Review not found")).when(reviewService).deleteReview(anyLong());

        mockMvc.perform(delete("/reviews/deleteReview/1"))
                .andExpect(status().isNotFound());
    }

}
