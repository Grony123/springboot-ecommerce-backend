package com.golang.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.golang.models.Product;
import com.golang.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mongo")
public class MongoController {
    @Autowired
    ProductRepository productRepository;

    @GetMapping("/products")
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @PostMapping("/product")
    public void addProduct(@RequestBody Product product) throws JsonProcessingException {
        System.out.println(new ObjectMapper().writeValueAsString(product));
        productRepository.insert(product);
    }
}
