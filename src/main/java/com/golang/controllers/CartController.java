package com.golang.controllers;

import com.golang.models.Cart;
import com.golang.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public Cart getCart(@AuthenticationPrincipal UserDetails user) {
        return cartService.getCart(user.getUsername());
    }

    @PostMapping("/add")
    public void addItem(@AuthenticationPrincipal UserDetails user,
                        @RequestParam String productId,
                        @RequestParam int quantity) {
        cartService.addItem(user.getUsername(), productId, quantity);
    }

    @DeleteMapping("/remove")
    public void removeItem(@AuthenticationPrincipal UserDetails user,
                           @RequestParam String productId) {
        cartService.removeItem(user.getUsername(), productId);
    }

    @DeleteMapping("/clear")
    public void clearCart(@AuthenticationPrincipal UserDetails user) {
        cartService.clearCart(user.getUsername());
    }
}

