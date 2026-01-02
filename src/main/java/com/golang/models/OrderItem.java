package com.golang.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {
    private String productId;
    private String productName;
    private Double price;
    private int quantity;
}