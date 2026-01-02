package com.golang.services;

import com.golang.models.CartItem;
import com.golang.models.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ProductService productService;

    private String getCartKey(String userEmail) {
        return "cart:" + userEmail;
    }

    public List<CartItem> getCart(String userEmail) {
        String key = getCartKey(userEmail);

        Map<Object, Object> redisMap = redisTemplate.opsForHash().entries(key);
        List<CartItem> items = new ArrayList<>();

        for (Map.Entry<Object, Object> entry : redisMap.entrySet()) {
            String productId = entry.getKey().toString();
            int quantity = Integer.parseInt(entry.getValue().toString());

            Product product = productService.getProductById(productId);
            items.add(new CartItem(product, quantity));
        }

        return items;
    }

    public void addItem(String userEmail, String productId, int quantity) {
        String cartKey = getCartKey(userEmail);
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();

        String existingQtyStr = hashOps.get(cartKey, productId);

        int existingQty = 0;
        if (existingQtyStr != null) {
            existingQty = Integer.parseInt(existingQtyStr);
        }

        int updatedQty = existingQty + quantity;

        hashOps.put(cartKey, productId, String.valueOf(updatedQty));
    }

    public void removeItem(String userEmail, String productId) {
        redisTemplate.opsForHash().delete(getCartKey(userEmail), productId);
    }

    public void clearCart(String userEmail) {
        redisTemplate.delete(getCartKey(userEmail));
    }
}
