package com.example.Jewelry.controller;

import com.example.Jewelry.dto.WishlistItemDTO;
import com.example.Jewelry.dto.response.CommonApiResponse;
import com.example.Jewelry.service.WishlistItemService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "http://localhost:3000")
public class WishlistItemController {

    @Autowired
    private WishlistItemService wishlistItemService;

    @PostMapping("/add")
    public ResponseEntity<CommonApiResponse> addToWishlist(@RequestParam int userId, @RequestParam int productId) {
        return ResponseEntity.ok(wishlistItemService.addToWishlist(userId, productId));
    }

    @DeleteMapping("/remove")
    @Operation(summary = "Xóa sản phẩm khỏi Wishlist")
    public ResponseEntity<CommonApiResponse> removeFromWishlist(
            @RequestParam int userId,
            @RequestParam int productId) {
        return ResponseEntity.ok(wishlistItemService.removeFromWishlist(userId, productId));
    }


    @GetMapping
    public ResponseEntity<List<WishlistItemDTO>> getUserWishlist(@RequestParam int userId) {
        List<WishlistItemDTO> wishlist = wishlistItemService.getWishlistByUser(userId);
        return ResponseEntity.ok(wishlist);
    }
}
