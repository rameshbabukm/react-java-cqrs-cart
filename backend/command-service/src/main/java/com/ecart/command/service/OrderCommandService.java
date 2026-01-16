package com.ecart.command.service;

import com.ecart.command.entity.*;
import com.ecart.command.repository.*;
import com.ecart.shared.events.OrderCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderCommandService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    
    @Transactional
    public Order createOrderFromCart(Long customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId);
        if (cart == null || cartItemRepository.findByCartId(cart.getId()).isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        Double totalPrice = 0.0;
        
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setStatus("PENDING");
        order.setCreatedAt(System.currentTimeMillis());
        
        for (CartItem item : items) {
            totalPrice += item.getPrice() * item.getQuantity();
            
            // Reduce product quantity
            Product product = productRepository.findById(item.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);
        }
        
        order.setTotalPrice(totalPrice);
        Order savedOrder = orderRepository.save(order);
        
        // Create order items
        for (CartItem item : items) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(savedOrder.getId());
            orderItem.setProductId(item.getProductId());
            orderItem.setProductName(item.getProductName());
            orderItem.setPrice(item.getPrice());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setCreatedAt(System.currentTimeMillis());
            orderItemRepository.save(orderItem);
        }
        
        // Clear cart
        items.forEach(cartItemRepository::delete);
        
        // Publish event to Kafka
        OrderCreatedEvent event = new OrderCreatedEvent(
            savedOrder.getId(), savedOrder.getCustomerId(), savedOrder.getTotalPrice(),
            savedOrder.getCreatedAt()
        );
        kafkaTemplate.send("order-events", event);
        
        return savedOrder;
    }
}
