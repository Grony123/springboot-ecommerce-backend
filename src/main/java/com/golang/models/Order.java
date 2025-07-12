package com.golang.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    private String userEmail;

    private List<OrderItem> items;

    private double totalPrice;

    private String status; // PENDING, PAID, SHIPPED, CANCELLED
}

