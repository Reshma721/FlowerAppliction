package com.floral.floralfiessy.testing.repository;

import com.floral.floralfiessy.entity.Product;
import com.floral.floralfiessy.entity.Review;
import com.floral.floralfiessy.entity.User;
import com.floral.floralfiessy.exception.UserNotFoundException;
import com.floral.floralfiessy.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ReviewRepositoryTest {

    @MockBean
    private ReviewRepository reviewRepository;

    private Review review;
    private User user;
    private Product product;

    @BeforeEach
    public void setUp() {
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
    }

    @Test
    public void testFindById_Success() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        Optional<Review> foundReview = reviewRepository.findById(1L);
        assertTrue(foundReview.isPresent());
        assertEquals(review.getId(), foundReview.get().getId());
    }

    @Test
    public void testFindById_Unsuccessful() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Review> foundReview = reviewRepository.findById(1L);
        assertFalse(foundReview.isPresent());
    }

    @Test
    public void testSaveReview_Success() {
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        Review savedReview = reviewRepository.save(review);
        assertNotNull(savedReview);
        assertEquals(review.getReview(), savedReview.getReview());
    }

    @Test
    public void testSaveReview_NullValue() {
        Review nullReview = new Review();
        when(reviewRepository.save(nullReview)).thenThrow(new IllegalArgumentException("Review cannot be null"));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reviewRepository.save(nullReview);
        });
        assertEquals("Review cannot be null", exception.getMessage());
    }

    @Test
    public void testDeleteReview_Success() {
        doNothing().when(reviewRepository).deleteById(1L);
        reviewRepository.deleteById(1L);
        verify(reviewRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteReview_Unsuccessful() {
        doThrow(new RuntimeException("Review not found")).when(reviewRepository).deleteById(1L);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            reviewRepository.deleteById(1L);
        });
        assertEquals("Review not found", exception.getMessage());
    }

    @Test
    public void testFindAll_Success() {
        Review review2 = new Review();
        review2.setId(2L);
        review2.setUser(user);
        review2.setProduct(product);
        review2.setReview("Another great product!");

        List<Review> reviews = Arrays.asList(review, review2);
        when(reviewRepository.findAll()).thenReturn(reviews);

        List<Review> foundReviews = reviewRepository.findAll();
        assertEquals(2, foundReviews.size());
        assertEquals("Great product!", foundReviews.get(0).getReview());
        assertEquals("Another great product!", foundReviews.get(1).getReview());
    }

    @Test
    public void testFindAll_Empty() {
        when(reviewRepository.findAll()).thenReturn(Arrays.asList());
        List<Review> foundReviews = reviewRepository.findAll();
        assertTrue(foundReviews.isEmpty());
    }

    @Test
    public void testUpdateReview_Success() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        review.setReview("Updated review");
        Review updatedReview = reviewRepository.save(review);

        assertEquals("Updated review", updatedReview.getReview());
    }

    @Test
    public void testUpdateReview_Unsuccessful() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            // Your update method in the service should call findById and save
            reviewRepository.findById(1L).orElseThrow(() -> new UserNotFoundException("Review not found with id: " + 1L));
            review.setReview("Updated review");
            reviewRepository.save(review);
        });

        assertEquals("Review not found with id: 1", exception.getMessage());
    }
}
