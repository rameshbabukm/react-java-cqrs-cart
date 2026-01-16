package com.ecart.query.service;

import com.ecart.shared.dto.ProductDTO;
import com.ecart.query.repository.ProductQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryProductService {
    
    @Autowired
    private ProductQueryRepository productRepository;
    
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll();
    }
    
    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
}
