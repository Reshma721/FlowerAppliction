package com.floral.floralfiessy.testing.service;

import com.floral.floralfiessy.dto.ReviewDto;
import com.floral.floralfiessy.entity.Product;
import com.floral.floralfiessy.entity.Review;
import com.floral.floralfiessy.entity.User;
import com.floral.floralfiessy.exception.UserNotFoundException;
import com.floral.floralfiessy.service.ReviewService;
import com.floral.floralfiessy.repository.ProductRepository;
import com.floral.floralfiessy.repository.ReviewRepository;
import com.floral.floralfiessy.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    private Review review;
    private ReviewDto reviewDto;
    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        product = new Product();
        product.setId(1L);
        product.setProductName("testproduct");

        review = new Review();
        review.setId(1L);
        review.setUser(user);
        review.setProduct(product);
        review.setReview("Great product!");

        reviewDto = new ReviewDto();
        reviewDto.setId(1L);
        reviewDto.setUserId(1L);
        reviewDto.setProductId(1L);
        reviewDto.setReview("Great product!");
    }

    @Test
    void addReview() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        reviewService.addReview(reviewDto);

        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void getAllReview() {
        when(reviewRepository.findAll()).thenReturn(Arrays.asList(review));

        List<ReviewDto> reviewDtos = reviewService.getAllReview();

        assertEquals(1, reviewDtos.size());
        assertEquals("Great product!", reviewDtos.get(0).getReview());
    }

    @Test
    void getAllReview_NoReviews() {
        when(reviewRepository.findAll()).thenReturn(Collections.emptyList());

        List<ReviewDto> reviewDtos = reviewService.getAllReview();

        assertTrue(reviewDtos.isEmpty());
    }

    @Test
    void getReviewById_ReviewExists() throws UserNotFoundException {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        ReviewDto foundReviewDto = reviewService.getReviewById(1L);

        assertEquals("Great product!", foundReviewDto.getReview());
    }

    @Test
    void getReviewById_ReviewDoesNotExist() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> reviewService.getReviewById(1L));
    }

    @Test
    void updateReview_ReviewExists() throws UserNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        reviewDto.setReview("Updated review!");

        reviewService.updateReview(1L, reviewDto);

        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void updateReview_ReviewDoesNotExist() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> reviewService.updateReview(1L, reviewDto));
    }

    @Test
    void deleteReview_ReviewExists() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        reviewService.deleteReview(1L);

        verify(reviewRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteReview_ReviewDoesNotExist() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reviewService.deleteReview(1L));
    }

    @Test
    void deleteReview_InvalidId() {
        doThrow(new EntityNotFoundException("Review not found with id 0")).when(reviewRepository).deleteById(0L);

        assertThrows(EntityNotFoundException.class, () -> reviewService.deleteReview(0L));
    }
}
