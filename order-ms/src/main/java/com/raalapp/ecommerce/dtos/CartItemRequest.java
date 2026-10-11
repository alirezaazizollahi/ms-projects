package com.raalapp.ecommerce.dtos;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long productId;
    private String userId;
    private Integer quantity;
}
