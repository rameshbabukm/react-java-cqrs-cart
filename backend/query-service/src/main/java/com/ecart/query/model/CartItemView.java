package com.ecart.query.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemView {
    private Long productId;
    private String productName;
    private Double price;
    private Integer quantity;
}
