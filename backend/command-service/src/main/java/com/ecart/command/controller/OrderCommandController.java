package com.ecart.command.controller;

import com.ecart.command.entity.Order;
import com.ecart.command.service.OrderCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@CrossOrigin(origins = {"http://localhost:3000", "http://frontend:3000"})
public class OrderCommandController {
    
    @Autowired
    private OrderCommandService orderService;
    
    @PostMapping("/{customerId}/checkout")
    public ResponseEntity<Order> checkout(@PathVariable Long customerId) {
        Order order = orderService.createOrderFromCart(customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
