package com.floral.floralfiessy.testing.repository;

import com.floral.floralfiessy.entity.Order;
import com.floral.floralfiessy.entity.Payment;
import com.floral.floralfiessy.entity.User;
import com.floral.floralfiessy.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PaymentRepositoryTest {

    @MockBean
    private PaymentRepository paymentRepository;

    private Payment payment;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Order order = new Order();
        order.setId(1L); // Set Order ID

        User user = new User();
        user.setId(1L); // Set User ID

        payment = new Payment();
        payment.setId(1L);
        payment.setPrice(100.0);
        payment.setPaymentdate(new java.util.Date());
        payment.setPaymentstatus("Completed");
        payment.setOrder(order);
        payment.setUser(user);
    }

    @Test
    public void testFindById_Success() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        Optional<Payment> foundPayment = paymentRepository.findById(1L);
        assertTrue(foundPayment.isPresent());
        assertEquals(payment.getId(), foundPayment.get().getId());
    }

    @Test
    public void testFindById_Unsuccessful() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Payment> foundPayment = paymentRepository.findById(1L);
        assertFalse(foundPayment.isPresent());
    }

    @Test
    public void testSavePayment_Success() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        Payment savedPayment = paymentRepository.save(payment);
        assertNotNull(savedPayment);
        assertEquals(payment.getPrice(), savedPayment.getPrice());
    }

    @Test
    public void testSavePayment_NullValue() {
        Payment nullPayment = new Payment();
        when(paymentRepository.save(nullPayment)).thenThrow(new IllegalArgumentException("Payment cannot be null"));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentRepository.save(nullPayment);
        });
        assertEquals("Payment cannot be null", exception.getMessage());
    }

    @Test
    public void testDeletePayment_Success() {
        doNothing().when(paymentRepository).deleteById(1L);
        paymentRepository.deleteById(1L);
        verify(paymentRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeletePayment_Unsuccessful() {
        doThrow(new RuntimeException("Payment not found")).when(paymentRepository).deleteById(1L);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            paymentRepository.deleteById(1L);
        });
        assertEquals("Payment not found", exception.getMessage());
    }

    @Test
    public void testFindAll_Success() {
        Payment payment2 = new Payment();
        payment2.setId(2L);
        payment2.setPrice(150.0);
        payment2.setPaymentdate(new java.util.Date());
        payment2.setPaymentstatus("Pending");
        payment2.setOrder(payment.getOrder());
        payment2.setUser(payment.getUser());

        List<Payment> payments = Arrays.asList(payment, payment2);
        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> foundPayments = paymentRepository.findAll();
        assertEquals(2, foundPayments.size());
        assertEquals(payment.getPrice(), foundPayments.get(0).getPrice());
        assertEquals(payment2.getPrice(), foundPayments.get(1).getPrice());
    }

    @Test
    public void testFindAll_Empty() {
        when(paymentRepository.findAll()).thenReturn(Arrays.asList());
        List<Payment> foundPayments = paymentRepository.findAll();
        assertTrue(foundPayments.isEmpty());
    }

    @Test
    public void testUpdatePayment_Success() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        payment.setPaymentstatus("Updated Status");
        Payment updatedPayment = paymentRepository.save(payment);

        assertEquals("Updated Status", updatedPayment.getPaymentstatus());
    }

    @Test
    public void testUpdatePayment_Unsuccessful() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            paymentRepository.findById(1L).orElseThrow(() -> new RuntimeException("Payment not found with id: " + 1L));
            payment.setPaymentstatus("Updated Status");
            paymentRepository.save(payment);
        });

        assertEquals("Payment not found with id: 1", exception.getMessage());
    }
}
