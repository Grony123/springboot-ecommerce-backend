package com.golang.services;

import com.golang.models.Order;
import com.golang.models.OrderItem;
import com.golang.models.Product;
import com.golang.repositories.OrderRepository;
import com.golang.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    private String getCartKey(String userEmail) {
        return "cart:" + userEmail;
    }

    public Order checkout(String userEmail) {
        // 1. Get cart
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        Map<String, String> cartEntries = hashOps.entries(getCartKey(userEmail));
        if (cartEntries.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        List<String> productIds = new ArrayList<>(cartEntries.keySet());

        List<Product> products = productRepository.findAllById(productIds);

        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId,p -> p));

        double totalPrice = 0;
        List<OrderItem> orderItems = new ArrayList<>();


        for (var entry : cartEntries.entrySet()) {
            String productId = entry.getKey();
            int quantity = Integer.parseInt(entry.getValue());

            Product p = productMap.get(productId);
            if (p == null) {
                throw new RuntimeException("Product not found: " + productId);
            }

            double itemTotal = p.getPrice() * quantity;
            totalPrice += itemTotal;

            // Build enriched OrderItem with name + price
            orderItems.add(OrderItem.builder()
                    .productId(productId)
                    .productName(p.getName())
                    .price(p.getPrice())
                    .quantity(quantity)
                    .build());
        }

        // 4. Save order
        Order order = Order.builder()
                .userEmail(userEmail)
                .items(orderItems)
                .totalPrice(totalPrice)
                .status("PENDING")
                .build();

        Order saved = orderRepository.save(order);

        // 5. Clear cart
        redisTemplate.delete(getCartKey(userEmail));

        return saved;
    }

    public List<Order> getUserOrders(String userEmail) {
        return orderRepository.findByUserEmail(userEmail);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order markAsPaid(String orderId, String userEmail) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUserEmail().equals(userEmail)) {
            throw new RuntimeException("You cannot pay for someone else's order");
        }

        order.setStatus("PAID");
        return orderRepository.save(order);
    }

    public Order updateStatus(String orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }
}
