package com.grocerystore.repository;

import com.grocerystore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    /**
     * Find all orders for a specific user
     * @param userId the user ID
     * @return list of orders for the user
     */
    List<Order> findByUserIdOrderByOrderDateDesc(String userId);
    
    /**
     * Find orders by status
     * @param status the order status
     * @return list of orders with the specified status
     */
    List<Order> findByStatusOrderByOrderDateDesc(Order.OrderStatus status);
    
    /**
     * Find orders by user and status
     * @param userId the user ID
     * @param status the order status
     * @return list of orders matching criteria
     */
    List<Order> findByUserIdAndStatusOrderByOrderDateDesc(String userId, Order.OrderStatus status);
    
    /**
     * Find orders within a date range
     * @param startDate start date
     * @param endDate end date
     * @return list of orders within the date range
     */
    List<Order> findByOrderDateBetweenOrderByOrderDateDesc(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find order with items loaded
     * @param orderId the order ID
     * @return optional order with items
     */
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.id = :orderId")
    Optional<Order> findByIdWithItems(@Param("orderId") Long orderId);
    
    /**
     * Find user orders with items loaded
     * @param userId the user ID
     * @return list of orders with items
     */
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.userId = :userId ORDER BY o.orderDate DESC")
    List<Order> findByUserIdWithItems(@Param("userId") String userId);
    
    /**
     * Count orders by user
     * @param userId the user ID
     * @return number of orders for the user
     */
    long countByUserId(String userId);
    
    /**
     * Find recent orders (last 30 days)
     * @param cutoffDate the cutoff date (30 days ago)
     * @return list of recent orders
     */
    @Query("SELECT o FROM Order o WHERE o.orderDate >= :cutoffDate ORDER BY o.orderDate DESC")
    List<Order> findRecentOrders(@Param("cutoffDate") LocalDateTime cutoffDate);
} 