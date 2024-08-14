package com.floral.floralfiessy.testing.repository;

import com.floral.floralfiessy.entity.Category;
import com.floral.floralfiessy.entity.Product;
import com.floral.floralfiessy.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductRepositoryTest {

    @Mock
    private ProductRepository productRepository;

    private Product product;
    private Category category;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        category = new Category();
        category.setId(1L);
        category.setCategoryname("Pet Supplies");

        product = new Product();
        product.setId(1L);
        product.setProductName("Bouquet");
        product.setDescription("Flower for marriage");
        product.setPrice(19.99);
        product.setQuantity(100);
        product.setCategory(category);
    }

    // Successful Tests

    @Test
    public void testSaveProduct_Successful() {
        when(productRepository.save(product)).thenReturn(product);

        Product savedProduct = productRepository.save(product);
        assertNotNull(savedProduct);
        assertEquals("Bouquet", savedProduct.getProductName());
    }

    @Test
    public void testFindProductById_Successful() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> foundProduct = productRepository.findById(1L);
        assertTrue(foundProduct.isPresent());
        assertEquals(1L, foundProduct.get().getId());
    }

    @Test
    public void testFindAllProducts_Successful() {
        Product product2 = new Product();
        product2.setId(2L);
        product2.setProductName("Flower Pot");
        product2.setCategory(category);

        when(productRepository.findAll()).thenReturn(Arrays.asList(product, product2));

        List<Product> products = productRepository.findAll();
        assertNotNull(products);
        assertEquals(2, products.size());
    }

    @Test
    public void testUpdateProduct_Successful() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product existingProduct = productRepository.findById(1L).get();
        existingProduct.setProductName("Pot full of flowers");

        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        Product updatedProduct = productRepository.save(existingProduct);
        assertNotNull(updatedProduct);
        assertEquals("Pot full of flowers", updatedProduct.getProductName());
    }

//    @Test
//    public void testDeleteProduct_Successful() {
//        // Arrange
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//        lenient().doNothing().when(productRepository).delete(product); // Use lenient stubbing
//
//        // Act
//        productRepository.delete(product);
//
//        // Assert
//        verify(productRepository, times(1)).delete(product);
//    }
    @Test
    void testDeleteProduct_Successful() {
        productRepository.delete(product);
        Optional<Product> deletedProduct = productRepository.findById(product.getId());
        assertThat(deletedProduct).isNotPresent();
    }


    // Unsuccessful Tests

    @Test
    public void testSaveProduct_Unsuccessful() {
        when(productRepository.save(product)).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            productRepository.save(product);
        });

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    public void testFindProductById_Unsuccessful() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Product> foundProduct = productRepository.findById(1L);
        assertFalse(foundProduct.isPresent());
    }

    @Test
    public void testFindAllProducts_Unsuccessful() {
        when(productRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            productRepository.findAll();
        });

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    public void testUpdateProduct_Unsuccessful() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            Product existingProduct = productRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            existingProduct.setProductName("Pot full of flowers");
            productRepository.save(existingProduct);
        });

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    public void testDeleteProduct_Unsuccessful() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            Product product = productRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            productRepository.delete(product);
        });

        assertEquals("Product not found", exception.getMessage());
    }
}
