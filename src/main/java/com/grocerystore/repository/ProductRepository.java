package com.grocerystore.repository;

import com.grocerystore.entity.Category;
import com.grocerystore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * Find all products by category
     * @param category the category to filter by
     * @return list of products in the specified category
     */
    List<Product> findByCategory(Category category);
    
    /**
     * Find products by name containing the search term (case insensitive)
     * @param name the search term
     * @return list of products matching the search
     */
    List<Product> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find all products ordered by name
     * @return list of all products ordered by name
     */
    List<Product> findAllByOrderByNameAsc();
} 