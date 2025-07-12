package com.golang.services;

import com.golang.models.Cart;
import com.golang.models.Order;
import com.golang.models.OrderItem;
import com.golang.repositories.OrderRepository;
import com.golang.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
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
        Cart cart = (Cart) redisTemplate.opsForValue().get(getCartKey(userEmail));
        if (cart == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // 2. Calculate total price
        double total = cart.getItems().entrySet().stream()
                .mapToDouble(entry -> {
                    var product = productRepository.findById(entry.getKey())
                            .orElseThrow(() -> new RuntimeException("Product not found"));
                    return product.getPrice() * entry.getValue();
                })
                .sum();

        // 3. Create OrderItems
        List<OrderItem> orderItems = cart.getItems().entrySet().stream()
                .map(entry -> new OrderItem(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        // 4. Save order
        Order order = Order.builder()
                .userEmail(userEmail)
                .items(orderItems)
                .totalPrice(total)
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
