package com.raalapp.ecommerce.controller;

import com.raalapp.ecommerce.dtos.CartItemRequest;
import com.raalapp.ecommerce.models.CartItem;
import com.raalapp.ecommerce.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestBody CartItemRequest request) {
        if (!cartService.addToCart(request)) {
            return ResponseEntity.badRequest().body("Not able to complete the request");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeFromCart(
            @RequestHeader("X-User-ID") Long userId,
            @PathVariable Long productId) {
        boolean deleted = cartService.deleteItemFromCart(userId, productId);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(
            @RequestHeader("X-User-ID") Long userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

}
