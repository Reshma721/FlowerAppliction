package com.floral.floralfiessy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class PaymentDto {
    private Long id;
    @NotNull(message = "Order ID shouldn't be blank")
    private Long orderId;
    @NotNull(message = "User ID shouldn't be blank")
    private Long userId;
    private double price; // No setter, only getter
    @NotNull(message = "Payment date shouldn't be blank")
    private Date paymentdate;
    @NotBlank(message = "Payment status shouldn't be blank")
    private String paymentstatus;
    @NotBlank(message = "Payment method shouldn't be blank")
    private String paymentmethod;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull(message = "Order ID shouldn't be blank") Long getOrderId() {
        return orderId;
    }

    public void setOrderId(@NotNull(message = "Order ID shouldn't be blank") Long orderId) {
        this.orderId = orderId;
    }

    public @NotNull(message = "User ID shouldn't be blank") Long getUserId() {
        return userId;
    }

    public void setUserId(@NotNull(message = "User ID shouldn't be blank") Long userId) {
        this.userId = userId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public @NotNull(message = "Payment date shouldn't be blank") Date getPaymentdate() {
        return paymentdate;
    }

    public void setPaymentdate(@NotNull(message = "Payment date shouldn't be blank") Date paymentdate) {
        this.paymentdate = paymentdate;
    }

    public @NotBlank(message = "Payment status shouldn't be blank") String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(@NotBlank(message = "Payment status shouldn't be blank") String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }

    public @NotBlank(message = "Payment method shouldn't be blank") String getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod(@NotBlank(message = "Payment method shouldn't be blank") String paymentmethod) {
        this.paymentmethod = paymentmethod;
    }
}
