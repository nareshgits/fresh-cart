package com.grocerystore.service;

import com.grocerystore.dto.AddToCartRequest;
import com.grocerystore.dto.CartResponse;
import com.grocerystore.entity.CartItem;
import com.grocerystore.entity.Product;
import com.grocerystore.repository.CartRepository;
import com.grocerystore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    /**
     * Get user's cart with complete product information
     * @param userId the user ID
     * @return cart response with items and totals
     */
    public CartResponse getCart(String userId) {
        List<CartItem> cartItems = cartRepository.findByUserIdWithProducts(userId);
        
        List<CartResponse.CartItemResponse> itemResponses = cartItems.stream()
                .map(CartResponse.CartItemResponse::new)
                .collect(Collectors.toList());

        Integer totalItems = cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        BigDecimal totalAmount = cartItems.stream()
                .filter(item -> item.getProduct() != null)
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(userId, itemResponses, totalItems, totalAmount);
    }

    /**
     * Add product to cart or update quantity if already exists
     * @param request the add to cart request
     * @return the cart item
     * @throws IllegalArgumentException if product doesn't exist
     */
    public CartItem addToCart(AddToCartRequest request) {
        // Validate that product exists
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + request.getProductId()));

        // Check if item already exists in cart
        Optional<CartItem> existingItem = cartRepository.findByUserIdAndProductId(
                request.getUserId(), request.getProductId());

        if (existingItem.isPresent()) {
            // Update quantity if item already exists
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
            return cartRepository.save(cartItem);
        } else {
            // Create new cart item
            CartItem newCartItem = new CartItem(
                    request.getProductId(),
                    request.getUserId(),
                    request.getQuantity()
            );
            return cartRepository.save(newCartItem);
        }
    }

    /**
     * Update cart item quantity
     * @param itemId the cart item ID
     * @param quantity the new quantity
     * @return the updated cart item
     * @throws IllegalArgumentException if cart item not found
     */
    public CartItem updateCartItemQuantity(Long itemId, Integer quantity) {
        CartItem cartItem = cartRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found with ID: " + itemId));

        cartItem.setQuantity(quantity);
        return cartRepository.save(cartItem);
    }

    /**
     * Remove item from cart
     * @param itemId the cart item ID
     * @param userId the user ID (for security check)
     * @return true if removed, false if not found or not owned by user
     */
    public boolean removeFromCart(Long itemId, String userId) {
        Optional<CartItem> cartItem = cartRepository.findById(itemId);
        
        if (cartItem.isPresent() && cartItem.get().getUserId().equals(userId)) {
            cartRepository.deleteById(itemId);
            return true;
        }
        return false;
    }

    /**
     * Remove item from cart (without user validation)
     * @param itemId the cart item ID
     * @return true if removed, false if not found
     */
    public boolean removeFromCart(Long itemId) {
        if (cartRepository.existsById(itemId)) {
            cartRepository.deleteById(itemId);
            return true;
        }
        return false;
    }

    /**
     * Clear all items from user's cart
     * @param userId the user ID
     */
    public void clearCart(String userId) {
        cartRepository.deleteByUserId(userId);
    }

    /**
     * Get cart item count for a user
     * @param userId the user ID
     * @return total number of items in cart
     */
    public Integer getCartItemCount(String userId) {
        return cartRepository.countItemsByUserId(userId);
    }

    /**
     * Check if product exists in user's cart
     * @param userId the user ID
     * @param productId the product ID
     * @return true if product is in cart
     */
    public boolean isProductInCart(String userId, Long productId) {
        return cartRepository.findByUserIdAndProductId(userId, productId).isPresent();
    }

    /**
     * Get cart item by ID with user validation
     * @param itemId the cart item ID
     * @param userId the user ID
     * @return optional cart item if found and owned by user
     */
    public Optional<CartItem> getCartItem(Long itemId, String userId) {
        return cartRepository.findById(itemId)
                .filter(item -> item.getUserId().equals(userId));
    }
} 