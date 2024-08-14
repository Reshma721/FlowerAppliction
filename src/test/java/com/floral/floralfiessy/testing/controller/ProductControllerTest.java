package com.floral.floralfiessy.testing.controller;

import com.floral.floralfiessy.controller.ProductController;
import com.floral.floralfiessy.dto.ProductDto;
import com.floral.floralfiessy.exception.UserNotFoundException;
import com.floral.floralfiessy.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setProductName("Bouquet");
        productDto.setDescription("Flower for marriage");
        productDto.setPrice(25.99);
        productDto.setQuantity(10);
        productDto.setAvailability("In Stock");
        productDto.setCategoryId(1L);
    }

    @Test
    void getAllProducts() throws Exception {
        List<ProductDto> productList = Arrays.asList(productDto);
        when(productService.getAllProducts()).thenReturn(productList);

        mockMvc.perform(get("/products/getAllProducts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Bouquet"));
    }

    @Test
    void getAllProducts_NoProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/products/getAllProducts"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getProductById_ProductExists() throws Exception {
        when(productService.getProductById(1L)).thenReturn(productDto);
        mockMvc.perform(get("/products/getProductById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Bouquet"));
    }

    @Test
    void getProductById_ProductDoesNotExist() throws Exception {
        when(productService.getProductById(1L)).thenThrow(new UserNotFoundException("Product not found"));

        mockMvc.perform(get("/products/getProductById/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addProduct() throws Exception {
        doNothing().when(productService).addProduct(any(ProductDto.class));

        mockMvc.perform(post("/products/addProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productName\":\"Dog Food\", \"description\":\"Food for dogs\", \"price\":25.99, \"quantity\":10, \"availability\":\"In Stock\", \"categoryId\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product added successfully!"));
    }


    @Test
    void updateProduct_ProductExists() throws Exception {
        doNothing().when(productService).updateProduct(anyLong(), any(ProductDto.class));

        mockMvc.perform(put("/products/updateProduct/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productName\":\"Dog Food\", \"description\":\"Food for dogs\", \"price\":25.99, \"quantity\":10, \"availability\":\"In Stock\", \"categoryId\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product updated successfully!"));
    }

    @Test
    void updateProduct_ProductDoesNotExist() throws Exception {
        doThrow(new UserNotFoundException("Product not found")).when(productService).updateProduct(anyLong(), any(ProductDto.class));

        mockMvc.perform(put("/products/updateProduct/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productName\":\"Dog Food\", \"description\":\"Food for dogs\", \"price\":25.99, \"quantity\":10, \"availability\":\"In Stock\", \"categoryId\":1}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProduct_ProductExists() throws Exception {
        doNothing().when(productService).deleteProduct(anyLong());

        mockMvc.perform(delete("/products/deleteProduct/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deleted successfully!"));
    }

    @Test
    void deleteProduct_ProductDoesNotExist() throws Exception {
        doThrow(new UserNotFoundException("Product not found")).when(productService).deleteProduct(anyLong());

        mockMvc.perform(delete("/products/deleteProduct/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProduct_InvalidIDFormat() throws Exception {
        mockMvc.perform(delete("/products/deleteProduct/invalid"))
                .andExpect(status().isBadRequest());
    }
}
