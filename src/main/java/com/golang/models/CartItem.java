package com.golang.models;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartItem {
    private Product product;
    private int quantity;
}
