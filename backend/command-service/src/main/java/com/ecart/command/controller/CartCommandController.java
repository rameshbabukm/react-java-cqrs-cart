package com.ecart.command.controller;

import com.ecart.command.service.CartCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/carts")
@CrossOrigin(origins = {"http://localhost:3000", "http://frontend:3000"})
public class CartCommandController {
    
    @Autowired
    private CartCommandService cartService;
    
    @PostMapping("/{customerId}/items")
    public ResponseEntity<Void> addItemToCart(
        @PathVariable Long customerId,
        @RequestBody Map<String, Object> request) {
        Long productId = Long.parseLong(request.get("productId").toString());
        Integer quantity = Integer.parseInt(request.get("quantity").toString());
        cartService.addItemToCart(customerId, productId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @DeleteMapping("/{customerId}/items/{productId}")
    public ResponseEntity<Void> removeItemFromCart(
        @PathVariable Long customerId,
        @PathVariable Long productId) {
        cartService.removeItemFromCart(customerId, productId);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{customerId}/items/{productId}")
    public ResponseEntity<Void> updateItemQuantity(
        @PathVariable Long customerId,
        @PathVariable Long productId,
        @RequestBody Map<String, Object> request) {
        Integer quantity = Integer.parseInt(request.get("quantity").toString());
        cartService.updateCartItemQuantity(customerId, productId, quantity);
        return ResponseEntity.ok().build();
    }
}
