package com.ecart.query.controller;

import com.ecart.query.model.OrderView;
import com.ecart.query.service.OrderQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = {"http://localhost:3000", "http://frontend:3000"})
public class OrderQueryController {
    
    @Autowired
    private OrderQueryService orderService;
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderView>> getOrdersByCustomerId(@PathVariable Long customerId) {
        List<OrderView> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OrderView> getOrderById(@PathVariable Long id) {
        OrderView order = orderService.getOrderById(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }
}
