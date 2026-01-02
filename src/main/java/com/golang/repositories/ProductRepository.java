package com.golang.repositories;

import com.golang.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByAddedByUserId(String userId);
}
