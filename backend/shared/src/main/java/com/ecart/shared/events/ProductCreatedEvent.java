package com.ecart.shared.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreatedEvent {
    private Long productId;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private Long categoryId;
    private Long timestamp;
}
