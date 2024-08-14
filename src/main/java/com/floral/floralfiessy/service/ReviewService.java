package com.floral.floralfiessy.service;

import com.floral.floralfiessy.exception.UserNotFoundException;
import com.floral.floralfiessy.dto.ReviewDto;
import com.floral.floralfiessy.entity.Product;
import com.floral.floralfiessy.entity.Review;
import com.floral.floralfiessy.entity.User;
import com.floral.floralfiessy.repository.ProductRepository;
import com.floral.floralfiessy.repository.ReviewRepository;
import com.floral.floralfiessy.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    private ReviewDto convertEntityToDTO(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(review.getId());
        reviewDto.setUserId(review.getUser().getId());
        reviewDto.setProductId(review.getProduct().getId());
        reviewDto.setReview(review.getReview());
        return reviewDto;
    }

    private Review convertDTOToEntity(ReviewDto reviewDto) throws UserNotFoundException {
        Review review = new Review();
        User user = userRepository.findById(reviewDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + reviewDto.getUserId()));
        review.setUser(user);
        Product product = productRepository.findById(reviewDto.getProductId())
                .orElseThrow(() -> new UserNotFoundException("Product not found with id: " + reviewDto.getProductId()));
        review.setProduct(product);
        review.setReview(reviewDto.getReview());
        return review;
    }

    public void addReview(ReviewDto reviewDto) throws UserNotFoundException {
        logger.info("Adding review: {}", reviewDto);
        Review review = convertDTOToEntity(reviewDto);
        reviewRepository.save(review);
    }

    public List<ReviewDto> getAllReview() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream().map(this::convertEntityToDTO).collect(Collectors.toList());
    }

    public void updateReview(Long id, ReviewDto reviewDto) throws UserNotFoundException {
        logger.info("Updating review with id: {}", id);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Review not found with id: " + id));
        User user = userRepository.findById(reviewDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + reviewDto.getUserId()));
        review.setUser(user);
        Product product = productRepository.findById(reviewDto.getProductId())
                .orElseThrow(() -> new UserNotFoundException("Product not found with id: " + reviewDto.getProductId()));
        review.setProduct(product);
        review.setReview(reviewDto.getReview());
        reviewRepository.save(review);
    }

    public void deleteReview(Long id) {
        logger.info("Deleting review with id: {}", id);
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isPresent()) {
            reviewRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Review not found with id " + id);
        }
    }

    public ReviewDto getReviewById(Long id) throws UserNotFoundException {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Review not found with id: " + id));
        return convertEntityToDTO(review);
    }
}
