package com.floral.floralfiessy.service;

import com.floral.floralfiessy.exception.UserNotFoundException;
import com.floral.floralfiessy.dto.OrderDto;
import com.floral.floralfiessy.entity.Cart;
import com.floral.floralfiessy.entity.Order;
import com.floral.floralfiessy.entity.Product;
import com.floral.floralfiessy.repository.CartRepository;
import com.floral.floralfiessy.repository.OrderRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    private OrderDto convertEntityToDTO(Order order) {
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(order, orderDto);
        orderDto.setCartId(order.getCart().getId());
        return orderDto;
    }

    private Order convertDTOToEntity(OrderDto orderDto) {
        Order order = new Order();
        BeanUtils.copyProperties(orderDto, order);
        return order;
    }

    public void addOrder(OrderDto orderDto) throws UserNotFoundException {
        Cart cart = cartRepository.findById(orderDto.getCartId())
                .orElseThrow(() -> new UserNotFoundException("Cart not found with id: " + orderDto.getCartId()));

        Order order = new Order();
        order.setCart(cart);
        order.setProductQuantities(orderDto.getProductQuantities());

        calculateOrderTotals(order);

        order.setOrderStatus(orderDto.getOrderStatus());
        orderRepository.save(order);
    }

    public List<OrderDto> getAllOrder() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::convertEntityToDTO).collect(Collectors.toList());
    }

    public void updateOrder(Long id, OrderDto orderDto) throws UserNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Order not found with id: " + id));
        Cart cart = cartRepository.findById(orderDto.getCartId())
                .orElseThrow(() -> new UserNotFoundException("Cart not found with id: " + orderDto.getCartId()));

        order.setCart(cart);
        order.setProductQuantities(orderDto.getProductQuantities());

        calculateOrderTotals(order);

        order.setOrderStatus(orderDto.getOrderStatus());
        orderRepository.save(order);
    }

    private void calculateOrderTotals(Order order) {
        double totalPrice = 0;
        int totalItem = 0;

        // Iterate over the product quantities in the order
        for (Map.Entry<Long, Integer> entry : order.getProductQuantities().entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();

            // Find the corresponding Product in the Cart
            for (Map.Entry<Product, Integer> cartEntry : order.getCart().getProducts().entrySet()) {
                if (cartEntry.getKey().getId().equals(productId)) {
                    Product product = cartEntry.getKey();
                    totalPrice += product.getPrice() * quantity;
                    totalItem += quantity;
                    break;
                }
            }
        }

        order.setTotalPrice(totalPrice);
        order.setTotalItem(totalItem);
    }

    public void deleteOrder(Long id) throws UserNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Order not found with id: " + id));
        orderRepository.deleteById(id);
    }

    public OrderDto getOrderById(Long id) throws UserNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Order not found with id: " + id));
        return convertEntityToDTO(order);
    }
}
