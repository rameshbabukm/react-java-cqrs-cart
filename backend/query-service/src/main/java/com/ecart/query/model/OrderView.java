package com.ecart.query.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderView {
    private Long id;
    private Long customerId;
    private Double totalPrice;
    private String status;
    private Long orderDate;
    private List<OrderItemView> items;
}
