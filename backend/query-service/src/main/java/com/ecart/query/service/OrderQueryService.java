package com.ecart.query.service;

import com.ecart.query.model.OrderView;
import com.ecart.query.repository.OrderQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderQueryService {
    
    @Autowired
    private OrderQueryRepository orderRepository;
    
    public List<OrderView> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
    
    public OrderView getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }
}
