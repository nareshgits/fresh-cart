package com.grocerystore.config;

import com.grocerystore.entity.CartItem;
import com.grocerystore.entity.Category;
import com.grocerystore.entity.Product;
import com.grocerystore.repository.CartRepository;
import com.grocerystore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    @Autowired
    public DataLoader(ProductRepository productRepository, CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only load sample data if the database is empty
        if (productRepository.count() == 0) {
            loadSampleData();
            loadSampleCartData();
        }
    }

    private void loadSampleData() {
        List<Product> sampleProducts = List.of(
                // Fruits
                new Product("Fresh Apples", Category.FRUITS, new BigDecimal("3.99"), 
                           "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=400&h=300&fit=crop&crop=center", 
                           "Crisp and sweet red apples, perfect for snacking or baking."),
                
                new Product("Organic Bananas", Category.FRUITS, new BigDecimal("2.49"), 
                           "https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=400&h=300&fit=crop&crop=center", 
                           "Naturally ripened organic bananas, rich in potassium."),
                
                new Product("Fresh Oranges", Category.FRUITS, new BigDecimal("4.99"), 
                           "https://images.unsplash.com/photo-1547514701-42782101795e?w=400&h=300&fit=crop&crop=center", 
                           "Juicy navel oranges packed with vitamin C."),
                
                new Product("Strawberries", Category.FRUITS, new BigDecimal("5.99"), 
                           "https://images.unsplash.com/photo-1464965911861-746a04b4bca6?w=400&h=300&fit=crop&crop=center", 
                           "Sweet and juicy strawberries, perfect for desserts."),

                // Vegetables
                new Product("Fresh Carrots", Category.VEGETABLES, new BigDecimal("1.99"), 
                           "https://images.unsplash.com/photo-1445282768818-728615cc910a?w=400&h=300&fit=crop&crop=center", 
                           "Crunchy orange carrots, great for snacking or cooking."),
                
                new Product("Organic Spinach", Category.VEGETABLES, new BigDecimal("3.49"), 
                           "https://images.unsplash.com/photo-1576045057995-568f588f82fb?w=400&h=300&fit=crop&crop=center", 
                           "Fresh organic spinach leaves, perfect for salads."),
                
                new Product("Bell Peppers", Category.VEGETABLES, new BigDecimal("4.49"), 
                           "https://images.unsplash.com/photo-1563565375-f3fdfdbefa83?w=400&h=300&fit=crop&crop=center", 
                           "Colorful bell peppers - red, yellow, and green mix."),
                
                new Product("Broccoli", Category.VEGETABLES, new BigDecimal("2.99"), 
                           "https://images.unsplash.com/photo-1459411621453-7b03977f4bfc?w=400&h=300&fit=crop&crop=center", 
                           "Fresh broccoli crowns, rich in vitamins and fiber."),

                // Dairy
                new Product("Whole Milk", Category.DAIRY, new BigDecimal("3.29"), 
                           "https://images.unsplash.com/photo-1550583724-b2692b85b150?w=400&h=300&fit=crop&crop=center", 
                           "Fresh whole milk, 1 gallon. Rich and creamy."),
                
                new Product("Greek Yogurt", Category.DAIRY, new BigDecimal("5.99"), 
                           "https://images.unsplash.com/photo-1488477181946-6428a0291777?w=400&h=300&fit=crop&crop=center", 
                           "Thick and creamy Greek yogurt, high in protein."),
                
                new Product("Cheddar Cheese", Category.DAIRY, new BigDecimal("4.99"), 
                           "https://images.unsplash.com/photo-1486297678162-eb2a19b0a32d?w=400&h=300&fit=crop&crop=center", 
                           "Sharp cheddar cheese block, aged to perfection."),
                
                new Product("Organic Butter", Category.DAIRY, new BigDecimal("6.49"), 
                           "https://images.unsplash.com/photo-1589985270826-4b7bb135bc9d?w=400&h=300&fit=crop&crop=center", 
                           "Creamy organic butter made from grass-fed cows."),

                // Beverages
                new Product("Orange Juice", Category.BEVERAGES, new BigDecimal("4.99"), 
                           "https://images.unsplash.com/photo-1621506289937-a8e4df240d0b?w=400&h=300&fit=crop&crop=center", 
                           "100% pure orange juice, not from concentrate."),
                
                new Product("Sparkling Water", Category.BEVERAGES, new BigDecimal("2.99"), 
                           "https://images.unsplash.com/photo-1523362628745-0c100150b504?w=400&h=300&fit=crop&crop=center", 
                           "Refreshing sparkling water with natural flavors."),
                
                new Product("Green Tea", Category.BEVERAGES, new BigDecimal("7.99"), 
                           "https://images.unsplash.com/photo-1556679343-c7306c1976bc?w=400&h=300&fit=crop&crop=center", 
                           "Premium green tea bags, antioxidant-rich."),
                
                new Product("Coffee Beans", Category.BEVERAGES, new BigDecimal("12.99"), 
                           "https://images.unsplash.com/photo-1559056199-641a0ac8b55e?w=400&h=300&fit=crop&crop=center", 
                           "Premium arabica coffee beans, medium roast.")
        );

        productRepository.saveAll(sampleProducts);
        System.out.println("Loaded " + sampleProducts.size() + " sample products into the database.");
    }

    private void loadSampleCartData() {
        // Add some sample cart items for testing
        List<CartItem> sampleCartItems = List.of(
                new CartItem(1L, "user123", 2), // 2 Fresh Apples
                new CartItem(2L, "user123", 1), // 1 Organic Bananas
                new CartItem(9L, "user123", 1), // 1 Whole Milk
                new CartItem(13L, "user123", 3), // 3 Orange Juice
                
                new CartItem(3L, "user456", 4), // 4 Fresh Oranges for different user
                new CartItem(7L, "user456", 2)  // 2 Bell Peppers for different user
        );

        cartRepository.saveAll(sampleCartItems);
        System.out.println("Loaded " + sampleCartItems.size() + " sample cart items into the database.");
    }
} 