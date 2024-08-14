package com.floral.floralfiessy.testing.service;

import com.floral.floralfiessy.dto.PaymentDto;
import com.floral.floralfiessy.entity.Order;
import com.floral.floralfiessy.entity.Payment;
import com.floral.floralfiessy.entity.User;
import com.floral.floralfiessy.exception.UserNotFoundException;
import com.floral.floralfiessy.service.PaymentService;
import com.floral.floralfiessy.repository.OrderRepository;
import com.floral.floralfiessy.repository.PaymentRepository;
import com.floral.floralfiessy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    private Payment payment;

    private PaymentDto paymentDto;

    private Order order;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        order = new Order();
        order.setId(1L); // Set ID or other properties if necessary

        user = new User();
        user.setId(1L); // Set ID or other properties if necessary

        Date now = new Date();
        payment = new Payment();
        payment.setId(1L);
        payment.setPrice(100.0);
        payment.setPaymentdate(now);
        payment.setPaymentstatus("Completed");
        payment.setOrder(order); // Set Order in Payment
        payment.setUser(user); // Set User in Payment

        paymentDto = new PaymentDto();
        paymentDto.setId(1L);
        paymentDto.setPrice(100.0);
        paymentDto.setPaymentdate(now);
        paymentDto.setPaymentstatus("Completed");
        paymentDto.setOrderId(order.getId()); // Ensure DTO has Order ID
        paymentDto.setUserId(user.getId()); // Ensure DTO has User ID
    }

    @Test
    void addPayment() throws UserNotFoundException {
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(orderRepository.findById(paymentDto.getOrderId())).thenReturn(Optional.of(order));
        when(userRepository.findById(paymentDto.getUserId())).thenReturn(Optional.of(user)); // Mock User

        paymentService.addPayment(paymentDto);

        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void getAllPayments() {
        when(paymentRepository.findAll()).thenReturn(Arrays.asList(payment));

        List<PaymentDto> paymentDtos = paymentService.getAllPayment();

        assertEquals(1, paymentDtos.size());
        assertEquals(100.0, paymentDtos.get(0).getPrice());
        assertNotNull(paymentDtos.get(0).getPaymentdate());
        assertEquals(order.getId(), paymentDtos.get(0).getOrderId());
        assertEquals(user.getId(), paymentDtos.get(0).getUserId());
    }

    @Test
    void getAllPayments_NoPayments() {
        when(paymentRepository.findAll()).thenReturn(Collections.emptyList());

        List<PaymentDto> paymentDtos = paymentService.getAllPayment();

        assertTrue(paymentDtos.isEmpty());
    }

    @Test
    void getPaymentById_PaymentExists() throws UserNotFoundException {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        PaymentDto foundPaymentDto = paymentService.getPaymentById(1L);

        assertEquals(100.0, foundPaymentDto.getPrice());
        assertNotNull(foundPaymentDto.getPaymentdate());
        assertEquals(order.getId(), foundPaymentDto.getOrderId());
        assertEquals(user.getId(), foundPaymentDto.getUserId());
    }

    @Test
    void getPaymentById_PaymentDoesNotExist() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> paymentService.getPaymentById(1L));
    }

    @Test
    void updatePayment_PaymentExists() throws UserNotFoundException {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(orderRepository.findById(paymentDto.getOrderId())).thenReturn(Optional.of(order));
        when(userRepository.findById(paymentDto.getUserId())).thenReturn(Optional.of(user)); // Mock User

        paymentDto.setPrice(150.0);
        paymentDto.setPaymentstatus("Pending");

        paymentService.updatePayment(1L, paymentDto);

        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void updatePayment_PaymentDoesNotExist() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> paymentService.updatePayment(1L, paymentDto));
    }

    @Test
    void deletePayment_PaymentExists() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        paymentService.deletePayment(1L);

        verify(paymentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePayment_PaymentDoesNotExist() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> paymentService.deletePayment(1L));
    }

    @Test
    void deletePayment_InvalidId() {
        doThrow(new UserNotFoundException("Payment not found with id 0")).when(paymentRepository).deleteById(0L);

        assertThrows(UserNotFoundException.class, () -> paymentService.deletePayment(0L));
    }
}
