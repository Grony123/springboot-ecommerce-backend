package com.golang.utils;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class TokenUtils {
    public static List<String> getCurrentUserRole() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return List.of(); // no roles
        }

        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }
}
