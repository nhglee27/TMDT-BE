package com.example.Jewelry.controller;

import com.example.Jewelry.dto.response.CommonApiResponse;
import com.example.Jewelry.entity.CartItem;
import com.example.Jewelry.service.CartItemService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cart")
@CrossOrigin(origins = "http://localhost:3000")
public class CartItemController {

    @Autowired
    private CartItemService cartService;

    @PostMapping("/add")
    @Operation(summary = "Thêm sản phẩm vào giỏ")
    public CartItem addToCart(@RequestParam int userId, @RequestParam int productId, @RequestParam(defaultValue = "1") int quantity) {
        return cartService.addToCart(userId, productId, quantity);
    }

    @GetMapping("/get")
    @Operation(summary = "Lấy tất cả sản phẩm trong giỏ của user")
    public List<CartItem> getCartItems(@RequestParam int userId) {
        return cartService.getCartItems(userId);
    }

    @DeleteMapping("/remove")
    @Operation(summary = "Xóa SP trong Cart")
    public ResponseEntity<CommonApiResponse> removeFromCart(
            @RequestParam int userId,
            @RequestParam int cartItemId) {
        return ResponseEntity.ok(cartService.removeFromCart(userId, cartItemId));
    }

    @DeleteMapping("/clear")
    @Operation(summary = "Xóa hết trong giỏ hàng")
    public ResponseEntity<CommonApiResponse> clearCart(@RequestParam int userId) {
        return ResponseEntity.ok(cartService.clearCart(userId));
    }

    @PutMapping("/update-quantity")
    @Operation(summary = "Cập nhật số lượng sản phẩm trong giỏ hàng")
    public ResponseEntity<CommonApiResponse> updateCartQuantity(
            @RequestParam int userId,
            @RequestParam int cartItemId,
            @RequestParam String action
    ) {
        CommonApiResponse response = cartService.updateQuantity(userId, cartItemId, action);
        return ResponseEntity.ok(response);
    }
}
