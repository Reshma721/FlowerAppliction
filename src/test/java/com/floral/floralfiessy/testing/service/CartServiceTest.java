package com.floral.floralfiessy.testing.service;

import com.floral.floralfiessy.dto.CartDto;
import com.floral.floralfiessy.entity.Cart;
import com.floral.floralfiessy.entity.Product;
import com.floral.floralfiessy.entity.User;
import com.floral.floralfiessy.service.CartService;
import com.floral.floralfiessy.repository.CartRepository;
import com.floral.floralfiessy.repository.ProductRepository;
import com.floral.floralfiessy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Cart createTestCart() {
        Cart cart = new Cart();
        User user = new User();
        user.setId(1L);
        cart.setUser(user);

        Product product = new Product();
        product.setId(1L);
        product.setPrice(10.0);

        cart.setProducts(Map.of(product, 2)); // Example with 2 items

        cart.setTotalItem(2); // Set total items
        cart.setTotalPrice(20.0); // Set total price

        return cart;
    }

    @Test
    void testAddCartSuccess() {
        CartDto cartDto = new CartDto();
        cartDto.setUserId(1L);
        cartDto.setProductQuantities(Map.of(1L, 1)); // Example product

        User user = new User();
        user.setId(1L);
        Product product = new Product();
        product.setId(1L);
        product.setPrice(10.0);

        Cart cart = new Cart();
        cart.setUser(user); // Ensure User is set
        cart.setProducts(Map.of(product, 1));
        cart.setTotalItem(1);
        cart.setTotalPrice(10.0);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CartDto result = cartService.addCart(cartDto);

        assertNotNull(result);
        assertEquals(1L, result.getUserId()); // Check if the User ID is correctly set
        assertEquals(1, result.getTotalItem());
        assertEquals(10.0, result.getTotalPrice());
    }

    @Test
    void testAddCartUserNotFound() {
        CartDto cartDto = createCartDto();

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> cartService.addCart(cartDto));
        assertEquals("User not found", thrown.getMessage());
    }

    @Test
    void testAddCartProductNotFound() {
        CartDto cartDto = createCartDto();
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> cartService.addCart(cartDto));
        assertEquals("Product not found: 1", thrown.getMessage());
    }

    @Test
    void testUpdateCartSuccess() {
        CartDto cartDto = createCartDto();
        Cart existingCart = new Cart();
        existingCart.setId(1L);
        User user = new User();
        user.setId(1L);
        Product product = new Product();
        product.setId(1L);
        product.setPrice(10.0);

        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(existingCart));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CartDto result = cartService.updateCart(1L, cartDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1, result.getTotalItem());
        assertEquals(10.0, result.getTotalPrice());
    }

    @Test
    void testUpdateCartNotFound() {
        CartDto cartDto = createCartDto();

        when(cartRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> cartService.updateCart(1L, cartDto));
        assertEquals("Cart not found", thrown.getMessage());
    }

    @Test
    void testUpdateCartUserNotFound() {
        CartDto cartDto = createCartDto();
        Cart existingCart = new Cart();

        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(existingCart));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> cartService.updateCart(1L, cartDto));
        assertEquals("User not found", thrown.getMessage());
    }

    @Test
    void testUpdateCartProductNotFound() {
        CartDto cartDto = createCartDto();
        Cart existingCart = new Cart();
        User user = new User();
        user.setId(1L);

        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(existingCart));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> cartService.updateCart(1L, cartDto));
        assertEquals("Product not found: 1", thrown.getMessage());
    }

    @Test
    void testGetCartByIdSuccess() {
        Cart cart = createTestCart();
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));

        CartDto result = cartService.getCartById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals(2, result.getTotalItem());
        assertEquals(20.0, result.getTotalPrice());
    }

    @Test
    void testGetCartByIdNotFound() {
        when(cartRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> cartService.getCartById(1L));
        assertEquals("Cart not found", thrown.getMessage());
    }

    @Test
    void testDeleteCartSuccess() {
        doNothing().when(cartRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> cartService.deleteCart(1L));
    }

    @Test
    void testDeleteCartNotFound() {
        doThrow(new RuntimeException("Cart not found")).when(cartRepository).deleteById(anyLong());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> cartService.deleteCart(1L));
        assertEquals("Cart not found", thrown.getMessage());
    }

    @Test
    void testGetAllCartsSuccess() {
        Cart cart = createTestCart();
        when(cartRepository.findAll()).thenReturn(List.of(cart)); // Return List instead of Set

        Set<CartDto> result = cartService.getAllCarts();

        assertNotNull(result);
        assertEquals(1, result.size());
        CartDto resultCart = result.iterator().next();
        assertEquals(1L, resultCart.getUserId()); // Check if the User ID is correctly set
        assertEquals(2, resultCart.getTotalItem());
        assertEquals(20.0, resultCart.getTotalPrice());
    }

    @Test
    void testCalculateCartTotals() {
        // Create a Cart and set the User and Products
        Cart cart = new Cart();
        User user = new User();
        user.setId(1L);
        cart.setUser(user);

        Product product = new Product();
        product.setId(1L);
        product.setPrice(10.0);
        cart.setProducts(Map.of(product, 2)); // Example with 2 items

        cart.setTotalItem(2); // Set total items
        cart.setTotalPrice(20.0); // Set total price

        CartDto cartDto = cartService.convertToDto(cart);

        assertNotNull(cartDto);
        assertEquals(1L, cartDto.getUserId());
        assertEquals(2, cartDto.getTotalItem());
        assertEquals(20.0, cartDto.getTotalPrice());
    }

    private CartDto createCartDto() {
        CartDto cartDto = new CartDto();
        cartDto.setId(1L);
        cartDto.setUserId(1L);
        Map<Long, Integer> productQuantities = new HashMap<>();
        productQuantities.put(1L, 1);
        cartDto.setProductQuantities(productQuantities);
        cartDto.setTotalItem(1);
        cartDto.setTotalPrice(10.0);
        return cartDto;
    }
}
