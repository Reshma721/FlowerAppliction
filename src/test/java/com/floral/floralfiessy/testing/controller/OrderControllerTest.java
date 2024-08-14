package com.floral.floralfiessy.testing.controller;

import com.floral.floralfiessy.dto.OrderDto;
import com.floral.floralfiessy.exception.UserNotFoundException;
import com.floral.floralfiessy.service.OrderService;
import com.floral.floralfiessy.controller.OrderController;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    public OrderControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllOrdersSuccess() {
        List<OrderDto> mockOrders = List.of(new OrderDto());
        when(orderService.getAllOrder()).thenReturn(mockOrders);

        ResponseEntity<List<OrderDto>> response = orderController.getAllOrders();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockOrders, response.getBody());
    }

    @Test
    void testGetAllOrdersFailure() {
        // Simulate an empty order list
        when(orderService.getAllOrder()).thenReturn(Collections.emptyList());

        // Call the getAllOrders method
        ResponseEntity<List<OrderDto>> response = orderController.getAllOrders();

        // Assert that the response status is 204 No Content or 200 OK with an empty body
        assertTrue(response.getStatusCode() == HttpStatus.NO_CONTENT || (response.getStatusCode() == HttpStatus.OK && response.getBody().isEmpty()));
    }


    @Test
    void testGetOrderByIdSuccess() throws UserNotFoundException {
        OrderDto mockOrder = new OrderDto();
        when(orderService.getOrderById(1L)).thenReturn(mockOrder);

        ResponseEntity<OrderDto> response = orderController.getOrderById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockOrder, response.getBody());
    }

    @Test
    void testGetOrderByIdNotFound() throws UserNotFoundException {
        when(orderService.getOrderById(1L)).thenThrow(new UserNotFoundException("Order not found with id: 1"));

        assertThrows(UserNotFoundException.class, () -> {
            orderController.getOrderById(1L);
        });
    }

    @Test
    void testAddOrderSuccess() throws UserNotFoundException {
        OrderDto orderDto = new OrderDto();
        doNothing().when(orderService).addOrder(orderDto);

        ResponseEntity<String> response = orderController.addOrder(orderDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Order added successfully!", response.getBody());
    }

    @Test
    void testAddOrderNotFound() throws UserNotFoundException {
        OrderDto orderDto = new OrderDto();
        doThrow(new UserNotFoundException("Cart not found with id: 1")).when(orderService).addOrder(orderDto);

        assertThrows(UserNotFoundException.class, () -> {
            orderController.addOrder(orderDto);
        });
    }

    @Test
    void testUpdateOrderSuccess() throws UserNotFoundException {
        OrderDto orderDto = new OrderDto();
        doNothing().when(orderService).updateOrder(1L, orderDto);

        ResponseEntity<String> response = orderController.updateOrder(1L, orderDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Order updated successfully!", response.getBody());
    }

    @Test
    void testUpdateOrderNotFound() throws UserNotFoundException {
        OrderDto orderDto = new OrderDto();
        doThrow(new UserNotFoundException("Order not found with id: 1")).when(orderService).updateOrder(1L, orderDto);

        assertThrows(UserNotFoundException.class, () -> {
            orderController.updateOrder(1L, orderDto);
        });
    }

    @Test
    void testDeleteOrderSuccess() throws UserNotFoundException {
        doNothing().when(orderService).deleteOrder(1L);

        ResponseEntity<String> response = orderController.deleteOrder(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Order deleted successfully!", response.getBody());
    }

    @Test
    void testDeleteOrderNotFound() throws UserNotFoundException {
        doThrow(new UserNotFoundException("Order not found with id: 1")).when(orderService).deleteOrder(1L);

        ResponseEntity<String> response = orderController.deleteOrder(1L);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Order not found with id: 1", response.getBody());
    }
}
