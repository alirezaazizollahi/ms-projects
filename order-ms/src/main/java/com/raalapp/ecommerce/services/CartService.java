package com.raalapp.ecommerce.services;


import com.raalapp.ecommerce.clients.ProductServiceClient;
import com.raalapp.ecommerce.clients.UserServiceClient;
import com.raalapp.ecommerce.dtos.CartItemRequest;
import com.raalapp.ecommerce.dtos.ProductResponse;
import com.raalapp.ecommerce.dtos.UserResponse;
import com.raalapp.ecommerce.models.CartItem;
import com.raalapp.ecommerce.repositories.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;
    private final CartItemRepository cartItemRepository;
    int attempt = 0;

//    @CircuitBreaker(name = "productService", fallbackMethod = "addToCartFallBack")
    public boolean addToCart(CartItemRequest request) {
        System.out.println("ATTEMPT COUNT: " + ++attempt);
        // Look for product

        ProductResponse productResponse = productServiceClient.getProductDetails(request.getProductId().toString());
        if (productResponse == null || productResponse.getStockQuantity() <  request.getQuantity())
            return false;

        UserResponse userDetails = userServiceClient.getUserDetails(request.getUserId().toString());
        if (userDetails == null)
            return false;

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(request.getUserId(), request.getProductId());
        if (existingCartItem != null) {
            // Update the quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepository.save(existingCartItem);
        } else {
            // Create new cart item
           CartItem cartItem = new CartItem();
           cartItem.setUserId(request.getUserId());
           cartItem.setProductId(request.getProductId());
           cartItem.setQuantity(request.getQuantity());
           cartItem.setPrice(BigDecimal.valueOf(1000.00));
           cartItemRepository.save(cartItem);
        }
        return true;
    }

    public boolean addToCartFallBack(String userId,
                                     CartItemRequest request,
                                     Exception exception) {
        exception.printStackTrace();
        return false;
    }

    public boolean deleteItemFromCart(String userId, Long productId) {
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);

        if (cartItem != null){
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;
    }

    public List<CartItem> getCart(String userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
