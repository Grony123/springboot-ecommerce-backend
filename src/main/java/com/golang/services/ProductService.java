package com.golang.services;

import com.golang.models.Product;
import com.golang.repositories.ProductRepository;
import com.golang.services.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.golang.utils.TokenUtils.getCurrentUserRole;

@Service
public class ProductService {
    @Autowired
    JwtService jwtService;
    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(Product product) {
        if (!isCurrentUserAdmin()) {
            throw new RuntimeException("User don't have admin access");
        }
        String token = jwtService.getCurrentJwtToken();
        product.setAddedByUserId(jwtService.extractUserId(token));
        return productRepository.save(product);
    }

    public List<Product> getProducts() {
        if(!isCurrentUserAdmin()){
            return productRepository.findAll();
        }
        return getProductsForAdmin();
    }

    private List<Product> getProductsForAdmin() {
        String token = jwtService.getCurrentJwtToken();
        String currentUser = jwtService.extractUserId(token);
        return productRepository.findByAddedByUserId(currentUser);
    }

    public Product getProductById(String id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product updateProduct(String id, Product updated) {
        if (!isCurrentUserAdmin()) {
            throw new RuntimeException("User don't have admin access");
        }

        Product existing = getProductById(id);
        if (existing == null) return null;

        if (!rightToAccess(existing)) {
            throw new RuntimeException("Current admin don't have rights for this product");
        }

        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setCategory(updated.getCategory());
        existing.setStock(updated.getStock());

        return productRepository.save(existing);
    }

    public void deleteProduct(String id) {
        if (!isCurrentUserAdmin()) {
            throw new RuntimeException("User don't have admin access");
        }

        Product existing = getProductById(id);
        if (existing == null) return;

        if (!rightToAccess(existing)) {
            throw new RuntimeException("Current admin don't have rights for this product");
        }
        productRepository.deleteById(id);
    }

    private boolean isCurrentUserAdmin() {
        List<String> roles = getCurrentUserRole();
        return roles.contains("ROLE_ADMIN");
    }

    private boolean rightToAccess(Product product) {
        String token = jwtService.getCurrentJwtToken();
        String currentUser = jwtService.extractUserId(token);
        return product.getAddedByUserId().equals(currentUser);
    }
}
