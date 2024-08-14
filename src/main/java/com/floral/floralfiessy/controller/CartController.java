package com.floral.floralfiessy.controller;

import com.floral.floralfiessy.dto.CartDto;
import com.floral.floralfiessy.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Validated
@RestController
@RequestMapping("/carts")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/getAllCarts")
    public ResponseEntity<Set<CartDto>> getAllCarts() {
        Set<CartDto> cartDtos=cartService.getAllCarts();
        return new ResponseEntity<>(cartDtos, HttpStatus.OK);
    }

    @GetMapping("/getCartById/{id}")
    public ResponseEntity<CartDto> getCartById(@PathVariable Long id) {
        try {
            CartDto cartDto = cartService.getCartById(id);
            return ResponseEntity.ok(cartDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PostMapping("/addCart")
    public ResponseEntity<String> addCart(@RequestBody @Valid CartDto cartDto) {
        try {
            cartService.addCart(cartDto);
            return ResponseEntity.ok("Cart added successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error adding cart: " + e.getMessage());
        }
    }

    @PutMapping("/updateCart/{id}")
    public ResponseEntity<String> updateCart(@PathVariable Long id, @RequestBody @Valid CartDto cartDto) {
        try {
            cartService.updateCart(id, cartDto);
            return ResponseEntity.ok("Cart updated successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found");
        }
    }

    @DeleteMapping("/deleteCart/{id}")
    public ResponseEntity<String> deleteCart(@PathVariable Long id) {
        try {
            cartService.deleteCart(id);
            return ResponseEntity.ok("Cart deleted successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found");
        }
    }
}
