package com.floral.floralfiessy.testing.repository;

import com.floral.floralfiessy.entity.Cart;
import com.floral.floralfiessy.entity.Order;
import com.floral.floralfiessy.exception.UserNotFoundException;
import com.floral.floralfiessy.repository.CartRepository;
import com.floral.floralfiessy.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderRepositoryTest {

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private CartRepository cartRepository;

    private Order order;
    private Cart cart;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up a Cart entity
        cart = new Cart();
        cart.setId(1L);
        cartRepository.save(cart);

        // Set up an Order entity
        order = new Order();
        order.setId(1L);
        order.setCart(cart);
        Map<Long, Integer> productQuantities = new HashMap<>();
        productQuantities.put(1L, 2);
        order.setProductQuantities(productQuantities);
        order.setTotalItem(2);
        order.setTotalPrice(100.0);
        order.setOrderStatus("Pending");
    }

    @Test
    public void testFindById_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        Optional<Order> foundOrder = orderRepository.findById(1L);
        assertTrue(foundOrder.isPresent());
        assertEquals(order.getId(), foundOrder.get().getId());
    }

    @Test
    public void testFindById_Unsuccessful() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Order> foundOrder = orderRepository.findById(1L);
        assertFalse(foundOrder.isPresent());
    }

    @Test
    public void testSaveOrder_Success() {
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        Order savedOrder = orderRepository.save(order);
        assertNotNull(savedOrder);
        assertEquals(order.getOrderStatus(), savedOrder.getOrderStatus());
    }

    @Test
    public void testSaveOrder_NullValue() {
        Order nullOrder = new Order();
        when(orderRepository.save(nullOrder)).thenThrow(new IllegalArgumentException("Order cannot be null"));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderRepository.save(nullOrder);
        });
        assertEquals("Order cannot be null", exception.getMessage());
    }

    @Test
    public void testDeleteOrder_Success() {
        doNothing().when(orderRepository).deleteById(1L);
        orderRepository.deleteById(1L);
        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteOrder_Unsuccessful() {
        doThrow(new RuntimeException("Order not found")).when(orderRepository).deleteById(1L);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            orderRepository.deleteById(1L);
        });
        assertEquals("Order not found", exception.getMessage());
    }

    @Test
    public void testFindAll_Success() {
        Order order2 = new Order();
        order2.setId(2L);
        order2.setCart(cart);
        order2.setProductQuantities(new HashMap<>());
        order2.setTotalItem(3);
        order2.setTotalPrice(150.0);
        order2.setOrderStatus("Confirmed");

        List<Order> orders = List.of(order, order2);
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> foundOrders = orderRepository.findAll();
        assertEquals(2, foundOrders.size());
        assertEquals("Pending", foundOrders.get(0).getOrderStatus());
        assertEquals("Confirmed", foundOrders.get(1).getOrderStatus());
    }

    @Test
    public void testFindAll_Empty() {
        when(orderRepository.findAll()).thenReturn(List.of());
        List<Order> foundOrders = orderRepository.findAll();
        assertTrue(foundOrders.isEmpty());
    }

    @Test
    public void testUpdateOrder_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        order.setOrderStatus("Updated status");
        Order updatedOrder = orderRepository.save(order);

        assertEquals("Updated status", updatedOrder.getOrderStatus());
    }

    @Test
    public void testUpdateOrder_Unsuccessful() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            orderRepository.findById(1L).orElseThrow(() -> new UserNotFoundException("Order not found with id: " + 1L));
            order.setOrderStatus("Updated status");
            orderRepository.save(order);
        });

        assertEquals("Order not found with id: 1", exception.getMessage());
    }
}
