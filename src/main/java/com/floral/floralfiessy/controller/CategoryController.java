package com.floral.floralfiessy.controller;

import com.floral.floralfiessy.dto.CategoryDto;
import com.floral.floralfiessy.exception.UserNotFoundException;
import com.floral.floralfiessy.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getAllCategory")
    public ResponseEntity<List<CategoryDto>> getAllCategory() {
        List<CategoryDto> categoryDtos = categoryService.getAllCategory();
        return new ResponseEntity<>(categoryDtos, HttpStatus.OK);
    }

    @GetMapping("/getCategoryById/{id}")
    public ResponseEntity<CategoryDto> getCategoriesById(@PathVariable Long id) {
        CategoryDto category = categoryService.getCategoryById(id);
        if (category == null) {
            throw new UserNotFoundException("Category not found");
        }
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping("/addCategory")
    public ResponseEntity<String> addCategory(@RequestBody @Valid CategoryDto categoryDto) {
        categoryService.addCategory(categoryDto);
        return ResponseEntity.ok("Category added successfully!");
    }

    @PutMapping("/updateCategory/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryDto categoryDto) {
        try {
            categoryService.updateCategory(id, categoryDto);
            return ResponseEntity.ok("Category updated successfully!");
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok("Category deleted successfully!");
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
