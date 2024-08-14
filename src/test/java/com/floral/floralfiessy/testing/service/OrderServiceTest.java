package com.floral.floralfiessy.testing.service;

import com.floral.floralfiessy.dto.OrderDto;
import com.floral.floralfiessy.entity.Cart;
import com.floral.floralfiessy.entity.Order;
import com.floral.floralfiessy.entity.Product;
import com.floral.floralfiessy.exception.UserNotFoundException;
import com.floral.floralfiessy.service.OrderService;
import com.floral.floralfiessy.repository.CartRepository;
import com.floral.floralfiessy.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private OrderDto createOrderDto(Long cartId, Map<Long, Integer> productQuantities, String orderStatus) {
        OrderDto orderDto = new OrderDto();
        orderDto.setCartId(cartId);
        orderDto.setProductQuantities(productQuantities);
        orderDto.setOrderStatus(orderStatus);
        return orderDto;
    }

    private Cart createCart() {
        Cart cart = new Cart();
        Map<Product, Integer> products = new HashMap<>();
        Product product = new Product();
        product.setId(1L);
        product.setPrice(10.0);
        products.put(product, 2); // Example Product
        cart.setProducts(products);
        return cart;
    }

    private Order createOrder(Cart cart, Map<Long, Integer> productQuantities, String orderStatus) {
        Order order = new Order();
        order.setCart(cart);
        order.setProductQuantities(productQuantities);
        order.setOrderStatus(orderStatus);
        return order;
    }

    @Test
    void testAddOrderSuccess() throws UserNotFoundException {
        OrderDto orderDto = createOrderDto(1L, Map.of(1L, 2), "Pending");
        Cart cart = createCart();
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        orderService.addOrder(orderDto);

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testAddOrderCartNotFound() {
        OrderDto orderDto = createOrderDto(1L, Map.of(1L, 2), "Pending");
        when(cartRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            orderService.addOrder(orderDto);
        });
    }

    @Test
    void testGetAllOrderSuccess() {
        Order order = createOrder(createCart(), Map.of(1L, 2), "Pending");
        List<Order> orders = List.of(order);
        when(orderRepository.findAll()).thenReturn(orders);

        List<OrderDto> result = orderService.getAllOrder();

        assertEquals(1, result.size());
        assertEquals(order.getOrderStatus(), result.get(0).getOrderStatus());
    }

    @Test
    void testUpdateOrderSuccess() throws UserNotFoundException {
        OrderDto orderDto = createOrderDto(1L, Map.of(1L, 2), "Shipped");
        Cart cart = createCart();
        Order order = createOrder(cart, Map.of(1L, 2), "Pending");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        orderService.updateOrder(1L, orderDto);

        verify(orderRepository, times(1)).save(order);
        assertEquals(orderDto.getOrderStatus(), order.getOrderStatus());
    }

    @Test
    void testUpdateOrderNotFound() {
        OrderDto orderDto = createOrderDto(1L, Map.of(1L, 2), "Shipped");
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            orderService.updateOrder(1L, orderDto);
        });
    }

    @Test
    void testDeleteOrderSuccess() throws UserNotFoundException {
        Order order = createOrder(createCart(), Map.of(1L, 2), "Pending");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.deleteOrder(1L);

        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            orderService.deleteOrder(1L);
        });
    }

    @Test
    void testGetOrderByIdSuccess() throws UserNotFoundException {
        Order order = createOrder(createCart(), Map.of(1L, 2), "Pending");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderDto result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(order.getOrderStatus(), result.getOrderStatus());
    }

    @Test
    void testGetOrderByIdNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            orderService.getOrderById(1L);
        });
    }
}
