package com.ecart.query.controller;

import com.ecart.query.model.CartView;
import com.ecart.query.service.CartQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@CrossOrigin(origins = {"http://localhost:3000", "http://frontend:3000"})
public class CartQueryController {
    
    @Autowired
    private CartQueryService cartService;
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<CartView> getCartByCustomerId(@PathVariable Long customerId) {
        CartView cart = cartService.getCartByCustomerId(customerId);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cart);
    }
}
