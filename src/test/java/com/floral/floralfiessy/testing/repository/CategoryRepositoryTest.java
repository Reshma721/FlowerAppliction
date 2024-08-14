package com.floral.floralfiessy.testing.repository;

import com.floral.floralfiessy.entity.Category;
import com.floral.floralfiessy.exception.UserNotFoundException;
import com.floral.floralfiessy.repository.CategoryRepository;
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
public class CategoryRepositoryTest {

    @MockBean
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        category = new Category();
        category.setId(1L);
        category.setCategoryname("Pets");
        category.setDescription("All about pets");
    }

    @Test
    public void testFindById_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        Optional<Category> foundCategory = categoryRepository.findById(1L);
        assertTrue(foundCategory.isPresent());
        assertEquals(category.getId(), foundCategory.get().getId());
    }

    @Test
    public void testFindById_Unsuccessful() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Category> foundCategory = categoryRepository.findById(1L);
        assertFalse(foundCategory.isPresent());
    }

    @Test
    public void testSaveCategory_Success() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        Category savedCategory = categoryRepository.save(category);
        assertNotNull(savedCategory);
        assertEquals(category.getCategoryname(), savedCategory.getCategoryname());
    }

    @Test
    public void testSaveCategory_NullValue() {
        Category nullCategory = new Category();
        when(categoryRepository.save(nullCategory)).thenThrow(new IllegalArgumentException("Category cannot be null"));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            categoryRepository.save(nullCategory);
        });
        assertEquals("Category cannot be null", exception.getMessage());
    }

    @Test
    public void testDeleteCategory_Success() {
        doNothing().when(categoryRepository).deleteById(1L);
        categoryRepository.deleteById(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteCategory_Unsuccessful() {
        doThrow(new RuntimeException("Category not found")).when(categoryRepository).deleteById(1L);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            categoryRepository.deleteById(1L);
        });
        assertEquals("Category not found", exception.getMessage());
    }

    @Test
    public void testFindAll_Success() {
        Category category2 = new Category();
        category2.setId(2L);
        category2.setCategoryname("Accessories");
        category2.setDescription("Pet accessories");

        List<Category> categories = Arrays.asList(category, category2);
        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> foundCategories = categoryRepository.findAll();
        assertEquals(2, foundCategories.size());
        assertEquals("Pets", foundCategories.get(0).getCategoryname());
        assertEquals("Accessories", foundCategories.get(1).getCategoryname());
    }

    @Test
    public void testFindAll_Empty() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList());
        List<Category> foundCategories = categoryRepository.findAll();
        assertTrue(foundCategories.isEmpty());
    }

    @Test
    public void testUpdateCategory_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        category.setDescription("Updated description");
        Category updatedCategory = categoryRepository.save(category);

        assertEquals("Updated description", updatedCategory.getDescription());
    }

    @Test
    public void testUpdateCategory_Unsuccessful() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            // Your update method in the service should call findById and save
            categoryRepository.findById(1L).orElseThrow(() -> new UserNotFoundException("Category not found with id: " + 1L));
            category.setDescription("Updated description");
            categoryRepository.save(category);
        });

        assertEquals("Category not found with id: 1", exception.getMessage());
    }
}
