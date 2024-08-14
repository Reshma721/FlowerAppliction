package com.floral.floralfiessy.service;

import com.floral.floralfiessy.exception.UserNotFoundException;
import com.floral.floralfiessy.dto.CategoryDto;
import com.floral.floralfiessy.entity.Category;
import com.floral.floralfiessy.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Convert entity to DTO
    private CategoryDto convertEntityToDTO(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        BeanUtils.copyProperties(category, categoryDto);
        return categoryDto;
    }

    // Convert DTO to entity
    private Category convertDTOToEntity(CategoryDto categoryDto) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDto, category);
        return category;
    }

    public void addCategory(CategoryDto categoryDto) {
        Category category = convertDTOToEntity(categoryDto);
        categoryRepository.save(category);
    }

    public List<CategoryDto> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(this::convertEntityToDTO).collect(Collectors.toList());
    }

    public void updateCategory(Long id, CategoryDto categoryDto) throws UserNotFoundException {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Category not found with id: " + id));
        category.setCategoryname(categoryDto.getCategoryname());
        category.setDescription(categoryDto.getDescription());
        categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            categoryRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Category not found with id " + id);
        }
    }

    public CategoryDto getCategoryById(Long id) throws UserNotFoundException {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Category not found with id: " + id));
        return convertEntityToDTO(category);
    }
}
