package com.ecart.query.repository;

import com.ecart.query.model.CartView;
import com.ecart.query.model.CartItemView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CartQueryRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public CartView findByCustomerId(Long customerId) {
        List<Long> cartIds = jdbcTemplate.queryForList(
            "SELECT id FROM carts WHERE customer_id = ?",
            Long.class,
            customerId
        );
        
        if (cartIds.isEmpty()) {
            return null;
        }
        
        Long cartId = cartIds.get(0);
        List<CartItemView> items = jdbcTemplate.query(
            "SELECT product_id, product_name, price, quantity FROM cart_items WHERE cart_id = ?",
            new Object[]{cartId},
            (rs, rowNum) -> new CartItemView(
                rs.getLong("product_id"),
                rs.getString("product_name"),
                rs.getDouble("price"),
                rs.getInt("quantity")
            )
        );
        
        Double totalPrice = items.stream()
            .mapToDouble(item -> item.getPrice() * item.getQuantity())
            .sum();
        
        CartView cart = new CartView();
        cart.setCustomerId(customerId);
        cart.setItems(items);
        cart.setTotalPrice(totalPrice);
        return cart;
    }
}
