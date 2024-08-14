package com.floral.floralfiessy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class CategoryDto {
    private Long id;
    @NotBlank(message = "category name shouldn't be blank")
    private String categoryname;
    @NotBlank(message = "description shouldn't be blank")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull(message = "category name shouldn't be blank") String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(@NotNull(message = "category name shouldn't be blank") String categoryname) {
        this.categoryname = categoryname;
    }

    public @NotNull(message = "description shouldn't be blank") String getDescription() {
        return description;
    }

    public void setDescription(@NotNull(message = "description shouldn't be blank") String description) {
        this.description = description;
    }
}
