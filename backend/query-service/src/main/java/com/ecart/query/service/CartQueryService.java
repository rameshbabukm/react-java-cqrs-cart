package com.ecart.query.service;

import com.ecart.query.model.CartView;
import com.ecart.query.repository.CartQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartQueryService {
    
    @Autowired
    private CartQueryRepository cartRepository;
    
    public CartView getCartByCustomerId(Long customerId) {
        return cartRepository.findByCustomerId(customerId);
    }
}
