package com.grocerystore.service;

import com.grocerystore.entity.Category;
import com.grocerystore.entity.Product;
import com.grocerystore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Get all products ordered by name
     * @return list of all products
     */
    public List<Product> getAllProducts() {
        return productRepository.findAllByOrderByNameAsc();
    }

    /**
     * Get products by category
     * @param category the category to filter by
     * @return list of products in the specified category
     */
    public List<Product> getProductsByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    /**
     * Get a product by ID
     * @param id the product ID
     * @return optional containing the product if found
     */
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * Create a new product
     * @param product the product to create
     * @return the created product
     */
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Update an existing product
     * @param id the product ID
     * @param productDetails the updated product details
     * @return the updated product if found, null otherwise
     */
    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(productDetails.getName());
                    product.setCategory(productDetails.getCategory());
                    product.setPrice(productDetails.getPrice());
                    product.setImageUrl(productDetails.getImageUrl());
                    product.setDescription(productDetails.getDescription());
                    return productRepository.save(product);
                })
                .orElse(null);
    }

    /**
     * Delete a product
     * @param id the product ID to delete
     * @return true if deleted, false if not found
     */
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Search products by name
     * @param name the search term
     * @return list of products matching the search
     */
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }
} 