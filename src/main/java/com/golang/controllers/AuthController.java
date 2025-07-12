package com.golang.controllers;

import com.golang.models.dtos.AuthResponse;
import com.golang.models.dtos.LoginRequest;
import com.golang.models.dtos.RegisterRequest;
import com.golang.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register/user")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody RegisterRequest request) {
        String token = authService.register(request,"USER");
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AuthResponse> registerAdmin(@RequestBody RegisterRequest request) {
        String token = authService.register(request,"ADMIN");
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}

