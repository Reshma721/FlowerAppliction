package com.floral.floralfiessy.entity;

import jakarta.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private String description;
    private double price;
    private int quantity;
    private String availability;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

//    @PostLoad
//    @PostPersist
//    @PostUpdate
//    public void updateAvailability() {
//        if (this.quantity > 0) {
//            this.availability = "In Stock";
//        } else {
//            this.availability = "Out of Stock";
//        }
//    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
//        updateAvailability();
    }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}
