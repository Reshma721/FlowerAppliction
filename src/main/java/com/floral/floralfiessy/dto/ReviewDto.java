package com.floral.floralfiessy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReviewDto {
    private Long id;
    @NotNull(message = "user Id shouldn't be blank")
    private Long userId;
    @NotNull(message = "product Id shouldn't be blank")
    private Long productId;
    @NotBlank(message = "review shouldn't be blank")
    private String review;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
