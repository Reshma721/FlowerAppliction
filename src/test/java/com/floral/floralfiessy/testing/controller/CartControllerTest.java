package com.floral.floralfiessy.testing.controller;

import com.floral.floralfiessy.controller.CartController;
import com.floral.floralfiessy.dto.CartDto;
import com.floral.floralfiessy.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CartControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CartController cartController;

    @Mock
    private CartService cartService;

    private CartDto cartDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();

        cartDto = new CartDto();
        cartDto.setId(1L);
        cartDto.setUserId(1L);
        Map<Long, Integer> productQuantities = new HashMap<>();
        productQuantities.put(1L, 2);
        cartDto.setProductQuantities(productQuantities);
        cartDto.setTotalItem(2);
        cartDto.setTotalPrice(100.0);
    }

    @Test
    void getAllCarts() throws Exception {
        Set<CartDto> cartSet = Collections.singleton(cartDto);
        when(cartService.getAllCarts()).thenReturn(cartSet);

        mockMvc.perform(get("/carts/getAllCarts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L));
    }

    @Test
    void getCartById_CartExists() throws Exception {
        when(cartService.getCartById(1L)).thenReturn(cartDto);

        mockMvc.perform(get("/carts/getCartById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L));
    }

    @Test
    void getCartById_CartDoesNotExist() throws Exception {
        when(cartService.getCartById(anyLong())).thenThrow(new RuntimeException("Cart not found"));

        mockMvc.perform(get("/carts/getCartById/2"))
                .andExpect(status().isNotFound());
    }


    @Test
    void addCart() throws Exception {
        when(cartService.addCart(any(CartDto.class))).thenReturn(cartDto);

        mockMvc.perform(post("/carts/addCart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"productQuantities\":{\"1\":2},\"totalItem\":2,\"totalPrice\":100.0}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Cart added successfully!"));
    }

    @Test
    void updateCart_CartExists() throws Exception {
        when(cartService.updateCart(anyLong(), any(CartDto.class))).thenReturn(cartDto);

        mockMvc.perform(put("/carts/updateCart/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"productQuantities\":{\"1\":2},\"totalItem\":2,\"totalPrice\":100.0}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Cart updated successfully!"));
    }

    @Test
    void updateCart_CartDoesNotExist() throws Exception {
        when(cartService.updateCart(anyLong(), any(CartDto.class))).thenThrow(new RuntimeException("Cart not found"));

        mockMvc.perform(put("/carts/updateCart/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"productQuantities\":{\"1\":2},\"totalItem\":2,\"totalPrice\":100.0}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCart_CartExists() throws Exception {
        doNothing().when(cartService).deleteCart(anyLong());

        mockMvc.perform(delete("/carts/deleteCart/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Cart deleted successfully!"));
    }

    @Test
    void deleteCart_CartDoesNotExist() throws Exception {
        doThrow(new RuntimeException("Cart not found")).when(cartService).deleteCart(anyLong());

        mockMvc.perform(delete("/carts/deleteCart/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCart_InvalidIDFormat() throws Exception {
        mockMvc.perform(delete("/carts/deleteCart/invalid"))
                .andExpect(status().isBadRequest());
    }
}
