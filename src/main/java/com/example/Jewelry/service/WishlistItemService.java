package com.example.Jewelry.service;

import com.example.Jewelry.dto.WishlistItemDTO;
import com.example.Jewelry.dto.response.CommonApiResponse;

import java.util.List;

public interface WishlistItemService {
    CommonApiResponse addToWishlist(int userId, int productId);
    CommonApiResponse removeFromWishlist(int userId, int productId);
    List<WishlistItemDTO> getWishlistByUser(int userId);
}
