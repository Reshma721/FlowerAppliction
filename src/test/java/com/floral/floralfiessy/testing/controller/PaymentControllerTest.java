package com.floral.floralfiessy.testing.controller;

import com.floral.floralfiessy.dto.PaymentDto;
import com.floral.floralfiessy.exception.UserNotFoundException;
import com.floral.floralfiessy.service.PaymentService;
import com.floral.floralfiessy.controller.PaymentController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PaymentControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private PaymentService paymentService;

    private PaymentDto paymentDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();

        paymentDto = new PaymentDto();
        paymentDto.setId(1L);
        paymentDto.setOrderId(1L);
        paymentDto.setUserId(1L);
        paymentDto.setPrice(100.0);
        paymentDto.setPaymentdate(new Date());
        paymentDto.setPaymentstatus("Pending");
        paymentDto.setPaymentmethod("Credit Card");
    }

    @Test
    void getAllPayments() throws Exception {
        List<PaymentDto> paymentList = Arrays.asList(paymentDto);
        when(paymentService.getAllPayment()).thenReturn(paymentList);

        mockMvc.perform(get("/payments/getAllPayments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value(100.0));
    }

    @Test
    void getAllPayments_NoPayments() throws Exception {
        when(paymentService.getAllPayment()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/payments/getAllPayments"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getPaymentById_PaymentExists() throws Exception {
        when(paymentService.getPaymentById(1L)).thenReturn(paymentDto);

        mockMvc.perform(get("/payments/getPaymentById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(100.0));
    }

    @Test
    void getPaymentById_PaymentDoesNotExist() throws Exception {
        when(paymentService.getPaymentById(anyLong())).thenThrow(new UserNotFoundException("Payment not found"));

        mockMvc.perform(get("/payments/getPaymentById/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Payment not found"));
    }


    @Test
    void addPayment() throws Exception {
        doNothing().when(paymentService).addPayment(any(PaymentDto.class));

        mockMvc.perform(post("/payments/addPayment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderId\":1,\"userId\":1,\"price\":100.0,\"paymentdate\":\"2024-07-30\",\"paymentstatus\":\"Pending\",\"paymentmethod\":\"Credit Card\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment added successfully!"));
    }

    @Test
    void updatePayment_PaymentExists() throws Exception {
        doNothing().when(paymentService).updatePayment(anyLong(), any(PaymentDto.class));

        mockMvc.perform(put("/payments/updatePayment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderId\":1,\"userId\":1,\"price\":100.0,\"paymentdate\":\"2024-07-30\",\"paymentstatus\":\"Pending\",\"paymentmethod\":\"Credit Card\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment updated successfully!"));
    }

    @Test
    void updatePayment_PaymentDoesNotExist() throws Exception {
        doThrow(new UserNotFoundException("Payment not found")).when(paymentService).updatePayment(anyLong(), any(PaymentDto.class));

        mockMvc.perform(put("/payments/updatePayment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderId\":1,\"userId\":1,\"price\":100.0,\"paymentdate\":\"2024-07-30\",\"paymentstatus\":\"Pending\",\"paymentmethod\":\"Credit Card\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePayment_PaymentExists() throws Exception {
        doNothing().when(paymentService).deletePayment(anyLong());

        mockMvc.perform(delete("/payments/deletePayment/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment deleted successfully!"));
    }

    @Test
    void deletePayment_PaymentDoesNotExist() throws Exception {
        doThrow(new UserNotFoundException("Payment not found")).when(paymentService).deletePayment(anyLong());

        mockMvc.perform(delete("/payments/deletePayment/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePayment_InvalidIDFormat() throws Exception {
        mockMvc.perform(delete("/payments/deletePayment/invalid"))
                .andExpect(status().isBadRequest());
    }
}
