package com.raalapp.ecommerce.dtos;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long productId;
    private Long userId;
    private Integer quantity;
}
