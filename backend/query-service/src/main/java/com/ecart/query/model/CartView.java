package com.ecart.query.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartView {
    private Long customerId;
    private Double totalPrice;
    private List<CartItemView> items;
}
