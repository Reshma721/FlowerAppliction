package com.floral.floralfiessy.controller;

import com.floral.floralfiessy.dto.OrderDto;
import com.floral.floralfiessy.exception.UserNotFoundException;
import com.floral.floralfiessy.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/getAllOrders")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orderDtos = orderService.getAllOrder();
        return ResponseEntity.ok(orderDtos);
    }

    @GetMapping("/getOrderById/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) throws UserNotFoundException {
        OrderDto orderDto = orderService.getOrderById(id);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping("/addOrder")
    public ResponseEntity<String> addOrder(@RequestBody @Valid OrderDto orderDto) throws UserNotFoundException {
        orderService.addOrder(orderDto);
        return ResponseEntity.ok("Order added successfully!");
    }

    @PutMapping("/updateOrder/{id}")
    public ResponseEntity<String> updateOrder(@PathVariable Long id, @RequestBody @Valid OrderDto orderDto) throws UserNotFoundException {
        orderService.updateOrder(id, orderDto);
        return ResponseEntity.ok("Order updated successfully!");
    }

    @DeleteMapping("/deleteOrder/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.ok("Order deleted successfully!");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting order: " + e.getMessage());
        }
    }
}
