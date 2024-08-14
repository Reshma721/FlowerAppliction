package com.floral.floralfiessy.controller;

import com.floral.floralfiessy.dto.PaymentDto;
import com.floral.floralfiessy.exception.UserNotFoundException;
import com.floral.floralfiessy.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/getAllPayments")
    public ResponseEntity<List<PaymentDto>> getAllPayments() {
        List<PaymentDto> paymentDtos = paymentService.getAllPayment();
        return new ResponseEntity<>(paymentDtos, HttpStatus.OK);
    }

//    @GetMapping("/getPaymentById/{id}")
//    public ResponseEntity<PaymentDto> getPaymentById(@PathVariable Long id) throws UserNotFoundException {
//        PaymentDto paymentDto = paymentService.getPaymentById(id);
//        return new ResponseEntity<>(paymentDto, HttpStatus.OK);
//    }
    @GetMapping("/getPaymentById/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable Long id) {
        try {
            PaymentDto paymentDto = paymentService.getPaymentById(id);
            return ResponseEntity.ok(paymentDto);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/addPayment")
    public ResponseEntity<String> addPayment(@RequestBody @Valid PaymentDto paymentDto) throws UserNotFoundException {
        paymentService.addPayment(paymentDto);
        return ResponseEntity.ok("Payment added successfully!");
    }
    @PutMapping("/updatePayment/{id}")
    public ResponseEntity<String> updatePayment(@PathVariable Long id, @RequestBody PaymentDto paymentDto) {
        try {
            paymentService.updatePayment(id, paymentDto);
            return ResponseEntity.ok("Payment updated successfully!");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/deletePayment/{id}")
    public ResponseEntity<String> deletePayment(@PathVariable Long id) {
        try {
            paymentService.deletePayment(id);
            return ResponseEntity.ok("Payment deleted successfully!");
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}

