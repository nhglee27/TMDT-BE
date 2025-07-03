package com.example.Jewelry.service;

import com.example.Jewelry.dto.response.CommonApiResponse;
import com.example.Jewelry.entity.CartItem;

import java.util.List;

public interface CartItemService {
    CartItem addToCart(int userId, int productId, int quantity);
    List<CartItem> getCartItems(int userId);
    CommonApiResponse clearCart(int userId);
    CommonApiResponse removeFromCart(int userId, int cartItemId);
    CommonApiResponse updateQuantity(int userId, int cartItemId, String action);
}

