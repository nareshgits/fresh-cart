package com.grocerystore.controller;

import com.grocerystore.dto.AddToCartRequest;
import com.grocerystore.dto.CartResponse;
import com.grocerystore.dto.UpdateCartRequest;
import com.grocerystore.entity.CartItem;
import com.grocerystore.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*") // For development - restrict in production
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * GET /api/cart?userId={userId} - Get current user's cart
     * @param userId the user ID (passed as query parameter)
     * @return user's cart with items and totals
     */
    @GetMapping
    public ResponseEntity<CartResponse> getCart(@RequestParam String userId) {
        try {
            CartResponse cart = cartService.getCart(userId);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * POST /api/cart - Add product to cart
     * @param request the add to cart request
     * @return the cart item response
     */
    @PostMapping
    public ResponseEntity<?> addToCart(@Valid @RequestBody AddToCartRequest request) {
        try {
            CartItem cartItem = cartService.addToCart(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(cartItem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding item to cart");
        }
    }

    /**
     * PUT /api/cart/{itemId}?userId={userId} - Update cart item quantity
     * @param itemId the cart item ID
     * @param userId the user ID (for security check)
     * @param request the update request
     * @return the updated cart item
     */
    @PutMapping("/{itemId}")
    public ResponseEntity<?> updateCartItem(
            @PathVariable Long itemId,
            @RequestParam String userId,
            @Valid @RequestBody UpdateCartRequest request) {
        try {
            // Verify the cart item belongs to the user
            if (!cartService.getCartItem(itemId, userId).isPresent()) {
                return ResponseEntity.notFound().build();
            }

            CartItem updatedItem = cartService.updateCartItemQuantity(itemId, request.getQuantity());
            return ResponseEntity.ok(updatedItem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating cart item");
        }
    }

    /**
     * DELETE /api/cart/{itemId}?userId={userId} - Remove item from cart
     * @param itemId the cart item ID
     * @param userId the user ID (for security check)
     * @return success or error response
     */
    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> removeFromCart(
            @PathVariable Long itemId,
            @RequestParam String userId) {
        try {
            boolean removed = cartService.removeFromCart(itemId, userId);
            if (removed) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing item from cart");
        }
    }

    /**
     * DELETE /api/cart/clear?userId={userId} - Clear all items from cart
     * @param userId the user ID
     * @return success response
     */
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@RequestParam String userId) {
        try {
            cartService.clearCart(userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error clearing cart");
        }
    }

    /**
     * GET /api/cart/count?userId={userId} - Get cart item count
     * @param userId the user ID
     * @return cart item count
     */
    @GetMapping("/count")
    public ResponseEntity<Integer> getCartItemCount(@RequestParam String userId) {
        try {
            Integer count = cartService.getCartItemCount(userId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/cart/check?userId={userId}&productId={productId} - Check if product is in cart
     * @param userId the user ID
     * @param productId the product ID
     * @return true if product is in cart
     */
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkProductInCart(
            @RequestParam String userId,
            @RequestParam Long productId) {
        try {
            boolean inCart = cartService.isProductInCart(userId, productId);
            return ResponseEntity.ok(inCart);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 