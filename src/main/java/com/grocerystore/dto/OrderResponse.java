package com.grocerystore.dto;

import com.grocerystore.entity.Order;
import com.grocerystore.entity.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private Long orderId;
    private String userId;
    private LocalDateTime orderDate;
    private Order.OrderStatus status;
    private String fullName;
    private String email;
    private String phone;
    private AddressInfo shippingAddress;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private List<OrderItemResponse> items;
    private Order.PaymentMethod paymentMethod;
    private String paymentTransactionId;
    private LocalDateTime estimatedDeliveryDate;
    private String deliveryInstructions;

    // Default constructor
    public OrderResponse() {}

    // Constructor from Order entity
    public OrderResponse(Order order) {
        this.orderId = order.getId();
        this.userId = order.getUserId();
        this.orderDate = order.getOrderDate();
        this.status = order.getStatus();
        this.fullName = order.getFullName();
        this.email = order.getEmail();
        this.phone = order.getPhone();
        this.shippingAddress = new AddressInfo(
                order.getAddressLine1(),
                order.getAddressLine2(),
                order.getCity(),
                order.getState(),
                order.getZipCode(),
                order.getCountry()
        );
        this.subtotal = order.getSubtotal();
        this.taxAmount = order.getTaxAmount();
        this.totalAmount = order.getTotalAmount();
        this.paymentMethod = order.getPaymentMethod();
        this.paymentTransactionId = order.getPaymentTransactionId();
        this.estimatedDeliveryDate = order.getEstimatedDeliveryDate();
        this.deliveryInstructions = order.getDeliveryInstructions();
        
        // Convert order items
        if (order.getOrderItems() != null) {
            this.items = order.getOrderItems().stream()
                    .map(OrderItemResponse::new)
                    .collect(Collectors.toList());
        }
    }

    // Inner class for address information
    public static class AddressInfo {
        private String addressLine1;
        private String addressLine2;
        private String city;
        private String state;
        private String zipCode;
        private String country;

        public AddressInfo() {}

        public AddressInfo(String addressLine1, String addressLine2, String city, 
                          String state, String zipCode, String country) {
            this.addressLine1 = addressLine1;
            this.addressLine2 = addressLine2;
            this.city = city;
            this.state = state;
            this.zipCode = zipCode;
            this.country = country;
        }

        // Getters and Setters
        public String getAddressLine1() { return addressLine1; }
        public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }
        public String getAddressLine2() { return addressLine2; }
        public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        public String getZipCode() { return zipCode; }
        public void setZipCode(String zipCode) { this.zipCode = zipCode; }
        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
    }

    // Inner class for order item response
    public static class OrderItemResponse {
        private Long id;
        private Long productId;
        private String productName;
        private String productDescription;
        private String productImageUrl;
        private String productCategory;
        private BigDecimal unitPrice;
        private Integer quantity;
        private BigDecimal subtotal;

        public OrderItemResponse() {}

        public OrderItemResponse(OrderItem orderItem) {
            this.id = orderItem.getId();
            this.productId = orderItem.getProductId();
            this.productName = orderItem.getProductName();
            this.productDescription = orderItem.getProductDescription();
            this.productImageUrl = orderItem.getProductImageUrl();
            this.productCategory = orderItem.getProductCategory() != null ? 
                    orderItem.getProductCategory().name() : null;
            this.unitPrice = orderItem.getUnitPrice();
            this.quantity = orderItem.getQuantity();
            this.subtotal = orderItem.getSubtotal();
        }

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        public String getProductDescription() { return productDescription; }
        public void setProductDescription(String productDescription) { this.productDescription = productDescription; }
        public String getProductImageUrl() { return productImageUrl; }
        public void setProductImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; }
        public String getProductCategory() { return productCategory; }
        public void setProductCategory(String productCategory) { this.productCategory = productCategory; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public BigDecimal getSubtotal() { return subtotal; }
        public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    }

    // Main class Getters and Setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public Order.OrderStatus getStatus() { return status; }
    public void setStatus(Order.OrderStatus status) { this.status = status; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public AddressInfo getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(AddressInfo shippingAddress) { this.shippingAddress = shippingAddress; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public List<OrderItemResponse> getItems() { return items; }
    public void setItems(List<OrderItemResponse> items) { this.items = items; }
    public Order.PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(Order.PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getPaymentTransactionId() { return paymentTransactionId; }
    public void setPaymentTransactionId(String paymentTransactionId) { this.paymentTransactionId = paymentTransactionId; }
    public LocalDateTime getEstimatedDeliveryDate() { return estimatedDeliveryDate; }
    public void setEstimatedDeliveryDate(LocalDateTime estimatedDeliveryDate) { this.estimatedDeliveryDate = estimatedDeliveryDate; }
    public String getDeliveryInstructions() { return deliveryInstructions; }
    public void setDeliveryInstructions(String deliveryInstructions) { this.deliveryInstructions = deliveryInstructions; }
} 