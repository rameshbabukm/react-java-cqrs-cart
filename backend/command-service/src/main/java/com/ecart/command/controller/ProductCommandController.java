package com.ecart.command.controller;

import com.ecart.command.service.ProductCommandService;
import com.ecart.shared.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = {"http://localhost:3000", "http://frontend:3000"})
public class ProductCommandController {
    
    @Autowired
    private ProductCommandService productService;
    
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(productService.createProduct(dto));
    }
}
