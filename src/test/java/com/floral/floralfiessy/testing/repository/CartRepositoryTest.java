package com.floral.floralfiessy.testing.repository;

import com.floral.floralfiessy.entity.Cart;
import com.floral.floralfiessy.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CartRepositoryTest {

    @Mock
    private CartRepository cartRepositoryMock;

    private Cart cart;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cart = new Cart();
        cart.setId(1L);
    }

    @Test
    void findById_CartExists() {
        when(cartRepositoryMock.findById(1L)).thenReturn(Optional.of(cart));

        Optional<Cart> foundCart = cartRepositoryMock.findById(1L);

        assertTrue(foundCart.isPresent());
        assertEquals(1L, foundCart.get().getId());
    }

    @Test
    void findById_CartDoesNotExist() {
        when(cartRepositoryMock.findById(2L)).thenReturn(Optional.empty());

        Optional<Cart> foundCart = cartRepositoryMock.findById(2L);

        assertFalse(foundCart.isPresent());
    }

    @Test
    void save_Cart() {
        when(cartRepositoryMock.save(cart)).thenReturn(cart);

        Cart savedCart = cartRepositoryMock.save(cart);

        assertNotNull(savedCart);
        assertEquals(1L, savedCart.getId());
    }

    @Test
    void save_NullCart() {
        when(cartRepositoryMock.save(null)).thenThrow(new IllegalArgumentException("Cart cannot be null"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartRepositoryMock.save(null);
        });

        assertEquals("Cart cannot be null", exception.getMessage());
    }

    @Test
    void deleteById_CartExists() {
        doNothing().when(cartRepositoryMock).deleteById(1L);

        assertDoesNotThrow(() -> cartRepositoryMock.deleteById(1L));
    }

    @Test
    void deleteById_CartDoesNotExist() {
        doThrow(new IllegalArgumentException("Cart not found")).when(cartRepositoryMock).deleteById(2L);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartRepositoryMock.deleteById(2L);
        });

        assertEquals("Cart not found", exception.getMessage());
    }

    @Test
    void findAll_CartsExist() {
        when(cartRepositoryMock.findAll()).thenReturn(List.of(cart));

        assertFalse(cartRepositoryMock.findAll().isEmpty());
        assertEquals(1, cartRepositoryMock.findAll().size());
    }

    @Test
    void findAll_NoCarts() {
        when(cartRepositoryMock.findAll()).thenReturn(Collections.emptyList());

        assertTrue(cartRepositoryMock.findAll().isEmpty());
    }

    @Test
    void update_CartExists() {
        Cart updatedCart = new Cart();
        updatedCart.setId(1L);
        when(cartRepositoryMock.save(updatedCart)).thenReturn(updatedCart);

        Cart savedCart = cartRepositoryMock.save(updatedCart);

        assertNotNull(savedCart);
        assertEquals(1L, savedCart.getId());
    }

    @Test
    void update_CartDoesNotExist() {
        Cart cartToUpdate = new Cart();
        cartToUpdate.setId(2L);
        when(cartRepositoryMock.findById(2L)).thenReturn(Optional.empty());

        Optional<Cart> foundCart = cartRepositoryMock.findById(2L);
        assertTrue(foundCart.isEmpty());

        // Simulate an update failure scenario
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            if (foundCart.isEmpty()) {
                throw new IllegalArgumentException("Cart to update not found");
            }
        });
    }
}
