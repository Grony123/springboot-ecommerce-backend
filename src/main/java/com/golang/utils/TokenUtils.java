package com.golang.utils;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class TokenUtils {
    public static List<String> getCurrentUserRole() {
        List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        return authorities.stream()
                .map(SimpleGrantedAuthority::toString)
                .toList();
    }
}
