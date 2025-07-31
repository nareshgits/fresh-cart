package com.grocerystore.repository;

import com.grocerystore.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {
    
    /**
     * Find all cart items for a specific user
     * @param userId the user ID
     * @return list of cart items for the user
     */
    List<CartItem> findByUserId(String userId);
    
    /**
     * Find a specific cart item by user and product
     * @param userId the user ID
     * @param productId the product ID
     * @return optional cart item if exists
     */
    Optional<CartItem> findByUserIdAndProductId(String userId, Long productId);
    
    /**
     * Delete all cart items for a specific user
     * @param userId the user ID
     */
    void deleteByUserId(String userId);
    
    /**
     * Count total items in a user's cart
     * @param userId the user ID
     * @return total number of items
     */
    @Query("SELECT COALESCE(SUM(c.quantity), 0) FROM CartItem c WHERE c.userId = :userId")
    Integer countItemsByUserId(@Param("userId") String userId);
    
    /**
     * Find cart items with product details for a user
     * @param userId the user ID
     * @return list of cart items with product information
     */
    @Query("SELECT c FROM CartItem c JOIN FETCH c.product p WHERE c.userId = :userId ORDER BY c.id")
    List<CartItem> findByUserIdWithProducts(@Param("userId") String userId);
} 