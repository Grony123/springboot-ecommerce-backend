package com.golang.services;

import com.golang.models.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartService {
    private final RedisTemplate<String, Object> redisTemplate;

    private String getCartKey(String userEmail) {
        return "cart:" + userEmail;
    }

    public Cart getCart(String userEmail) {
        Cart cart = (Cart) redisTemplate.opsForValue().get(getCartKey(userEmail));
        if (cart == null) {
            cart = new Cart(userEmail, new HashMap<>());
        }
        return cart;
    }

    public void addItem(String userEmail, String productId, int quantity) {
        Cart cart = getCart(userEmail);
        cart.getItems().put(productId, quantity);
        redisTemplate.opsForValue().set(getCartKey(userEmail), cart);
    }

    public void removeItem(String userEmail, String productId) {
        Cart cart = getCart(userEmail);
        cart.getItems().remove(productId);
        redisTemplate.opsForValue().set(getCartKey(userEmail), cart);
    }

    public void clearCart(String userEmail) {
        redisTemplate.delete(getCartKey(userEmail));
    }
}
