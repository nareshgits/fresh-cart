package com.grocerystore.service;

import com.grocerystore.dto.CheckoutRequest;
import com.grocerystore.dto.OrderResponse;
import com.grocerystore.entity.Order;
import com.grocerystore.entity.OrderItem;
import com.grocerystore.entity.Product;
import com.grocerystore.repository.OrderRepository;
import com.grocerystore.repository.ProductRepository;
import com.grocerystore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    
    // Tax rate (8%)
    private static final BigDecimal TAX_RATE = new BigDecimal("0.08");

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
    }

    /**
     * Process checkout and create order
     * @param checkoutRequest the checkout request with address and payment info
     * @return order response with confirmation details
     */
    public OrderResponse processCheckout(CheckoutRequest checkoutRequest) {
        // 1. Get user's cart
        var cartData = cartService.getCart(checkoutRequest.getUserId());
        
        if (cartData.getItems() == null || cartData.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty. Cannot process checkout.");
        }

        // 2. Create new order
        Order order = new Order();
        order.setUserId(checkoutRequest.getUserId());
        
        // Set customer information
        order.setFullName(checkoutRequest.getFullName());
        order.setEmail(checkoutRequest.getEmail());
        order.setPhone(checkoutRequest.getPhone());
        
        // Set shipping address
        order.setAddressLine1(checkoutRequest.getAddressLine1());
        order.setAddressLine2(checkoutRequest.getAddressLine2());
        order.setCity(checkoutRequest.getCity());
        order.setState(checkoutRequest.getState());
        order.setZipCode(checkoutRequest.getZipCode());
        order.setCountry(checkoutRequest.getCountry());
        
        // Set payment information
        order.setPaymentMethod(checkoutRequest.getPaymentMethod());
        order.setPaymentTransactionId(checkoutRequest.getPaymentTransactionId() != null ? 
                checkoutRequest.getPaymentTransactionId() : 
                generateTransactionId());
        
        // Set delivery information
        order.setDeliveryInstructions(checkoutRequest.getDeliveryInstructions());
        order.setEstimatedDeliveryDate(calculateEstimatedDeliveryDate());
        
        // 3. Calculate totals
        BigDecimal subtotal = cartData.getTotalAmount();
        BigDecimal taxAmount = subtotal.multiply(TAX_RATE);
        BigDecimal totalAmount = subtotal.add(taxAmount);
        
        order.setSubtotal(subtotal);
        order.setTaxAmount(taxAmount);
        order.setTotalAmount(totalAmount);
        
        // 4. Save order first to get ID
        order = orderRepository.save(order);
        
        // 5. Create order items from cart items
        List<OrderItem> orderItems = new ArrayList<>();
        for (var cartItem : cartData.getItems()) {
            // Get current product details
            Optional<Product> productOpt = productRepository.findById(cartItem.getProductId());
            if (productOpt.isEmpty()) {
                throw new IllegalArgumentException("Product not found: " + cartItem.getProductId());
            }
            
            Product product = productOpt.get();
            
            OrderItem orderItem = new OrderItem(
                    order,
                    cartItem.getProductId(),
                    cartItem.getProductName(),
                    cartItem.getProductPrice(),
                    cartItem.getQuantity(),
                    product.getDescription(),
                    product.getImageUrl(),
                    product.getCategory()
            );
            
            orderItems.add(orderItem);
        }
        
        order.setOrderItems(orderItems);
        
        // 6. Save order with items
        order = orderRepository.save(order);
        
        // 7. Process payment simulation
        boolean paymentSuccess = processPayment(order);
        if (!paymentSuccess) {
            order.setStatus(Order.OrderStatus.CANCELLED);
            orderRepository.save(order);
            throw new RuntimeException("Payment processing failed. Order has been cancelled.");
        }
        
        // 8. Confirm order
        order.setStatus(Order.OrderStatus.CONFIRMED);
        order = orderRepository.save(order);
        
        // 9. Clear user's cart after successful order
        cartService.clearCart(checkoutRequest.getUserId());
        
        // 10. Return order response
        return new OrderResponse(order);
    }

    /**
     * Get order by ID
     * @param orderId the order ID
     * @return order response
     */
    public Optional<OrderResponse> getOrderById(Long orderId) {
        return orderRepository.findByIdWithItems(orderId)
                .map(OrderResponse::new);
    }

    /**
     * Get user's orders
     * @param userId the user ID
     * @return list of user orders
     */
    public List<OrderResponse> getUserOrders(String userId) {
        List<Order> orders = orderRepository.findByUserIdWithItems(userId);
        return orders.stream()
                .map(OrderResponse::new)
                .toList();
    }

    /**
     * Update order status
     * @param orderId the order ID
     * @param status the new status
     * @return updated order response
     */
    public Optional<OrderResponse> updateOrderStatus(Long orderId, Order.OrderStatus status) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setStatus(status);
                    Order savedOrder = orderRepository.save(order);
                    return new OrderResponse(savedOrder);
                });
    }

    /**
     * Cancel order
     * @param orderId the order ID
     * @param userId the user ID (for security)
     * @return true if cancelled, false if not found or not owned by user
     */
    public boolean cancelOrder(Long orderId, String userId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            
            // Check if order belongs to user and can be cancelled
            if (order.getUserId().equals(userId) && 
                (order.getStatus() == Order.OrderStatus.PENDING || 
                 order.getStatus() == Order.OrderStatus.CONFIRMED)) {
                
                order.setStatus(Order.OrderStatus.CANCELLED);
                orderRepository.save(order);
                return true;
            }
        }
        
        return false;
    }

    /**
     * Get order count for user
     * @param userId the user ID
     * @return number of orders
     */
    public long getUserOrderCount(String userId) {
        return orderRepository.countByUserId(userId);
    }

    // Private helper methods

    private LocalDateTime calculateEstimatedDeliveryDate() {
        // For demo: 3-5 business days
        return LocalDateTime.now().plusDays(4);
    }

    private String generateTransactionId() {
        // Generate a simple transaction ID for demo purposes
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private boolean processPayment(Order order) {
        // Simulate payment processing
        // In real application, this would integrate with payment gateway
        
        switch (order.getPaymentMethod()) {
            case CREDIT_CARD:
            case DEBIT_CARD:
                // Simulate credit/debit card processing
                return simulateCardPayment(order);
            
            case PAYPAL:
                // Simulate PayPal processing
                return simulatePayPalPayment(order);
            
            case CASH_ON_DELIVERY:
                // Cash on delivery - always succeeds
                return true;
            
            default:
                return false;
        }
    }

    private boolean simulateCardPayment(Order order) {
        // For demo: 95% success rate
        // In real app, this would call Stripe, Square, etc.
        return Math.random() > 0.05; // 95% success rate
    }

    private boolean simulatePayPalPayment(Order order) {
        // For demo: 98% success rate
        return Math.random() > 0.02; // 98% success rate
    }
} 