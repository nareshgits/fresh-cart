package com.grocerystore.dto;

import com.grocerystore.entity.CartItem;

import java.math.BigDecimal;
import java.util.List;

public class CartResponse {

    private String userId;
    private List<CartItemResponse> items;
    private Integer totalItems;
    private BigDecimal totalAmount;

    // Default constructor
    public CartResponse() {}

    // Constructor
    public CartResponse(String userId, List<CartItemResponse> items, Integer totalItems, BigDecimal totalAmount) {
        this.userId = userId;
        this.items = items;
        this.totalItems = totalItems;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CartItemResponse> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponse> items) {
        this.items = items;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    // Inner class for cart item response
    public static class CartItemResponse {
        private Long id;
        private Long productId;
        private String productName;
        private BigDecimal productPrice;
        private String productImageUrl;
        private String productCategory;
        private String productDescription;
        private Integer quantity;
        private BigDecimal subtotal;

        // Default constructor
        public CartItemResponse() {}

        // Constructor
        public CartItemResponse(CartItem cartItem) {
            this.id = cartItem.getId();
            this.productId = cartItem.getProductId();
            this.quantity = cartItem.getQuantity();
            
            if (cartItem.getProduct() != null) {
                this.productName = cartItem.getProduct().getName();
                this.productPrice = cartItem.getProduct().getPrice();
                this.productImageUrl = cartItem.getProduct().getImageUrl();
                this.productCategory = cartItem.getProduct().getCategory().name();
                this.productDescription = cartItem.getProduct().getDescription();
                this.subtotal = cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(quantity));
            }
        }

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public BigDecimal getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(BigDecimal productPrice) {
            this.productPrice = productPrice;
        }

        public String getProductImageUrl() {
            return productImageUrl;
        }

        public void setProductImageUrl(String productImageUrl) {
            this.productImageUrl = productImageUrl;
        }

        public String getProductCategory() {
            return productCategory;
        }

        public void setProductCategory(String productCategory) {
            this.productCategory = productCategory;
        }

        public String getProductDescription() {
            return productDescription;
        }

        public void setProductDescription(String productDescription) {
            this.productDescription = productDescription;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(BigDecimal subtotal) {
            this.subtotal = subtotal;
        }
    }
} 