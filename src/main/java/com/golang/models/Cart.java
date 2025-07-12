package com.golang.models;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@Getter
@Setter
public class Cart {
    private String userEmail;
    private Map<String, Integer> items;

    public Cart(String userEmail, Map<String, Integer> items) {
        this.userEmail = userEmail;
        this.items = items;
    }
}
