package com.ecart.command.service;

import com.ecart.command.entity.Product;
import com.ecart.command.repository.ProductRepository;
import com.ecart.shared.dto.ProductDTO;
import com.ecart.shared.events.ProductCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ProductCommandService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    
    public ProductDTO createProduct(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setCategoryId(dto.getCategoryId());
        product.setCreatedAt(System.currentTimeMillis());
        
        Product saved = productRepository.save(product);
        
        // Publish event to Kafka
        ProductCreatedEvent event = new ProductCreatedEvent(
            saved.getId(), saved.getName(), saved.getDescription(),
            saved.getPrice(), saved.getQuantity(), saved.getCategoryId(), saved.getCreatedAt()
        );
        kafkaTemplate.send("product-events", event);
        
        dto.setId(saved.getId());
        return dto;
    }
    
    public void updateProductQuantity(Long productId, Integer newQuantity) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setQuantity(newQuantity);
        productRepository.save(product);
    }
}
