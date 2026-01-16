package com.ecart.command.service;

import com.ecart.command.entity.*;
import com.ecart.command.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartCommandService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    
    @Transactional
    public void addItemToCart(Long customerId, Long productId, Integer quantity) {
        Cart cart = cartRepository.findByCustomerId(customerId);
        if (cart == null) {
            cart = new Cart();
            cart.setCustomerId(customerId);
            cart.setCreatedAt(System.currentTimeMillis());
            cart = cartRepository.save(cart);
        }
        
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        
        CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
            .orElse(null);
        
        if (item == null) {
            item = new CartItem();
            item.setCartId(cart.getId());
            item.setProductId(productId);
            item.setProductName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(quantity);
            item.setCreatedAt(System.currentTimeMillis());
        } else {
            item.setQuantity(item.getQuantity() + quantity);
        }
        
        cartItemRepository.save(item);
        cart.setUpdatedAt(System.currentTimeMillis());
        cartRepository.save(cart);
    }
    
    @Transactional
    public void removeItemFromCart(Long customerId, Long productId) {
        Cart cart = cartRepository.findByCustomerId(customerId);
        if (cart != null) {
            cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .ifPresent(cartItemRepository::delete);
        }
    }
    
    @Transactional
    public void updateCartItemQuantity(Long customerId, Long productId, Integer quantity) {
        Cart cart = cartRepository.findByCustomerId(customerId);
        if (cart != null) {
            cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    cartItemRepository.save(item);
                });
        }
    }
    
    public void clearCart(Long customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId);
        if (cart != null) {
            cartItemRepository.findByCartId(cart.getId())
                .forEach(cartItemRepository::delete);
        }
    }
}
