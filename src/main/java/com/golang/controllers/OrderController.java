package com.golang.controllers;

import com.golang.models.Order;
import com.golang.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public Order checkout(@AuthenticationPrincipal UserDetails user) {
        return orderService.checkout(user.getUsername());
    }

    @GetMapping
    public List<Order> getUserOrders(@AuthenticationPrincipal UserDetails user) {
        return orderService.getUserOrders(user.getUsername());
    }

    @GetMapping("/all")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping("/{id}/pay")
    public Order pay(@PathVariable String id,
                     @AuthenticationPrincipal UserDetails user) {
        return orderService.markAsPaid(id, user.getUsername());
    }

    @PutMapping("/{id}/status")
    public Order updateStatus(@PathVariable String id,
                              @RequestParam String status) {
        return orderService.updateStatus(id, status);
    }
}
