package com.grocerystore.controller;

import com.grocerystore.dto.CheckoutRequest;
import com.grocerystore.dto.OrderResponse;
import com.grocerystore.entity.Order;
import com.grocerystore.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "*") // For development - restrict in production
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * POST /api/order/checkout - Process checkout and create order
     * @param checkoutRequest the checkout request with address and payment info
     * @return order confirmation response
     */
    @PostMapping("/checkout")
    public ResponseEntity<?> processCheckout(@Valid @RequestBody CheckoutRequest checkoutRequest) {
        try {
            OrderResponse orderResponse = orderService.processCheckout(checkoutRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Checkout error: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body("Payment error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing your order. Please try again.");
        }
    }

    /**
     * GET /api/order/{orderId} - Get order by ID
     * @param orderId the order ID
     * @return order details
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId)
                .map(order -> ResponseEntity.ok(order))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/order/user/{userId} - Get user's orders
     * @param userId the user ID
     * @return list of user's orders
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getUserOrders(@PathVariable String userId) {
        try {
            List<OrderResponse> orders = orderService.getUserOrders(userId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * PUT /api/order/{orderId}/status - Update order status (admin only)
     * @param orderId the order ID
     * @param status the new status
     * @return updated order
     */
    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam Order.OrderStatus status) {
        try {
            return orderService.updateOrderStatus(orderId, status)
                    .map(order -> ResponseEntity.ok(order))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating order status: " + e.getMessage());
        }
    }

    /**
     * DELETE /api/order/{orderId}/cancel - Cancel order
     * @param orderId the order ID
     * @param userId the user ID (for security)
     * @return success or error response
     */
    @DeleteMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(
            @PathVariable Long orderId,
            @RequestParam String userId) {
        try {
            boolean cancelled = orderService.cancelOrder(orderId, userId);
            if (cancelled) {
                return ResponseEntity.ok().body("Order cancelled successfully");
            } else {
                return ResponseEntity.badRequest().body("Unable to cancel order. Order may not exist, not belong to user, or cannot be cancelled in current status.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error cancelling order: " + e.getMessage());
        }
    }

    /**
     * GET /api/order/user/{userId}/count - Get user's order count
     * @param userId the user ID
     * @return order count
     */
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> getUserOrderCount(@PathVariable String userId) {
        try {
            long count = orderService.getUserOrderCount(userId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/order/health - Health check endpoint
     * @return service status
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Order service is running");
    }
} 