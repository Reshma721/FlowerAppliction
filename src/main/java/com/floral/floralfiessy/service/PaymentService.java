package com.floral.floralfiessy.service;

import com.floral.floralfiessy.exception.UserNotFoundException;
import com.floral.floralfiessy.dto.PaymentDto;
import com.floral.floralfiessy.entity.Order;
import com.floral.floralfiessy.entity.Payment;
import com.floral.floralfiessy.entity.User;
import com.floral.floralfiessy.repository.OrderRepository;
import com.floral.floralfiessy.repository.PaymentRepository;
import com.floral.floralfiessy.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    private PaymentDto convertEntityToDTO(Payment payment) {
        PaymentDto paymentDto = new PaymentDto();
        BeanUtils.copyProperties(payment, paymentDto);
        paymentDto.setOrderId(payment.getOrder().getId());
        paymentDto.setUserId(payment.getUser().getId());
        paymentDto.setPrice(payment.getPrice()); // Set the price in DTO
        return paymentDto;
    }

    private Payment convertDTOToEntity(PaymentDto paymentDto) throws UserNotFoundException {
        Payment payment = new Payment();
        BeanUtils.copyProperties(paymentDto, payment);
        Order order = orderRepository.findById(paymentDto.getOrderId())
                .orElseThrow(() -> new UserNotFoundException("Order not found with id: " + paymentDto.getOrderId()));
        payment.setOrder(order);
        User user = userRepository.findById(paymentDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + paymentDto.getUserId()));
        payment.setUser(user);
        payment.setPrice(order.getTotalPrice()); // Calculate price from order
        return payment;
    }

    public void addPayment(PaymentDto paymentDto) throws UserNotFoundException {
        Payment payment = convertDTOToEntity(paymentDto);
        paymentRepository.save(payment);
    }

    public List<PaymentDto> getAllPayment() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream().map(this::convertEntityToDTO).collect(Collectors.toList());
    }

    public void updatePayment(Long id, PaymentDto paymentDto) throws UserNotFoundException {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Payment not found with id: " + id));
        BeanUtils.copyProperties(paymentDto, payment, "id", "price");
        Order order = orderRepository.findById(paymentDto.getOrderId())
                .orElseThrow(() -> new UserNotFoundException("Order not found with id: " + paymentDto.getOrderId()));
        payment.setOrder(order);
        User user = userRepository.findById(paymentDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + paymentDto.getUserId()));
        payment.setUser(user);
        payment.setPrice(order.getTotalPrice()); // Calculate price from order
        paymentRepository.save(payment);
    }

    public void deletePayment(Long id) throws UserNotFoundException{
        Optional<Payment> payment = paymentRepository.findById(id);
        if (payment.isPresent()) {
            paymentRepository.deleteById(id);
        } else {
            throw new UserNotFoundException("Payment not found with id " + id);
        }
    }

    public PaymentDto getPaymentById(Long id) throws UserNotFoundException {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Payment not found with id: " + id));
        return convertEntityToDTO(payment);
    }
}
