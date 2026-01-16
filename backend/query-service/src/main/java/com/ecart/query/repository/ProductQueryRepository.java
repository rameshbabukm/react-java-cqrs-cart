package com.ecart.query.repository;

import com.ecart.shared.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductQueryRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public List<ProductDTO> findAll() {
        return jdbcTemplate.query(
            "SELECT id, name, description, price, quantity, category_id FROM products",
            (rs, rowNum) -> new ProductDTO(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDouble("price"),
                rs.getInt("quantity"),
                rs.getLong("category_id")
            )
        );
    }
    
    public ProductDTO findById(Long id) {
        List<ProductDTO> result = jdbcTemplate.query(
            "SELECT id, name, description, price, quantity, category_id FROM products WHERE id = ?",
            new Object[]{id},
            (rs, rowNum) -> new ProductDTO(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDouble("price"),
                rs.getInt("quantity"),
                rs.getLong("category_id")
            )
        );
        return result.isEmpty() ? null : result.get(0);
    }
    
    public List<ProductDTO> findByCategoryId(Long categoryId) {
        return jdbcTemplate.query(
            "SELECT id, name, description, price, quantity, category_id FROM products WHERE category_id = ?",
            new Object[]{categoryId},
            (rs, rowNum) -> new ProductDTO(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDouble("price"),
                rs.getInt("quantity"),
                rs.getLong("category_id")
            )
        );
    }
}
