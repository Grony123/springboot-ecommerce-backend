package com.golang.services;

import com.golang.exception.auth.EmailAlreadyExistsException;
import com.golang.models.User;
import com.golang.models.dtos.AuthResponse;
import com.golang.models.dtos.LoginRequest;
import com.golang.models.dtos.RegisterRequest;
import com.golang.repositories.UserRepository;
import com.golang.services.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public String register(RegisterRequest request, String role) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered");
        }
        User user = User.builder()
                .username(request.getEmail())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        userRepository.save(user);
        return jwtService.generateToken(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return new AuthResponse(jwtService.generateToken(user),user.getRole());
    }
}
