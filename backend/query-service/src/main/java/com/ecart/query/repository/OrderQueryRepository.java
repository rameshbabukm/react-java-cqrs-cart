package com.ecart.query.repository;

import com.ecart.query.model.OrderView;
import com.ecart.query.model.OrderItemView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderQueryRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public List<OrderView> findByCustomerId(Long customerId) {
        List<Long> orderIds = jdbcTemplate.queryForList(
            "SELECT id FROM orders WHERE customer_id = ?",
            Long.class,
            customerId
        );
        
        return orderIds.stream()
            .map(this::findById)
            .toList();
    }
    
    public OrderView findById(Long orderId) {
        List<OrderView> result = jdbcTemplate.query(
            "SELECT id, customer_id, total_price, status, created_at FROM orders WHERE id = ?",
            new Object[]{orderId},
            (rs, rowNum) -> {
                OrderView order = new OrderView();
                order.setId(rs.getLong("id"));
                order.setCustomerId(rs.getLong("customer_id"));
                order.setTotalPrice(rs.getDouble("total_price"));
                order.setStatus(rs.getString("status"));
                order.setOrderDate(rs.getLong("created_at"));
                return order;
            }
        );
        
        if (result.isEmpty()) {
            return null;
        }
        
        OrderView order = result.get(0);
        List<OrderItemView> items = jdbcTemplate.query(
            "SELECT product_id, product_name, price, quantity FROM order_items WHERE order_id = ?",
            new Object[]{orderId},
            (rs, rowNum) -> new OrderItemView(
                rs.getLong("product_id"),
                rs.getString("product_name"),
                rs.getDouble("price"),
                rs.getInt("quantity")
            )
        );
        
        order.setItems(items);
        return order;
    }
}
