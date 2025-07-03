package com.example.Jewelry.service.ServiceImpl;

import com.example.Jewelry.dao.WishlistItemDAO;
import com.example.Jewelry.dao.UserDAO;
import com.example.Jewelry.dao.ProductDAO;
import com.example.Jewelry.dto.ProductDTO;
import com.example.Jewelry.dto.WishlistItemDTO;
import com.example.Jewelry.dto.response.CommonApiResponse;
import com.example.Jewelry.entity.Product;
import com.example.Jewelry.entity.User;
import com.example.Jewelry.entity.WishlistItem;
import com.example.Jewelry.service.WishlistItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistItemServiceImpl implements WishlistItemService {

    private final WishlistItemDAO wishlistItemDAO;
    private final UserDAO userDAO;
    private final ProductDAO productDAO;

    @Override
    public CommonApiResponse addToWishlist(int userId, int productId) {
        User user = userDAO.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productDAO.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<WishlistItem> existing = wishlistItemDAO.findByUserAndProductAndDeletedFalse(user, product);
        if (existing.isPresent()) {
            return CommonApiResponse.builder()
                    .isSuccess(false)
                    .responseMessage("Sản phẩm đã có trong wishlist")
                    .build();
        }

        WishlistItem wishlistItem = WishlistItem.builder()
                .user(user)
                .product(product)
                .createdAt(LocalDateTime.now())
                .deleted(false)
                .build();

        WishlistItem savedItem = wishlistItemDAO.save(wishlistItem);
        convertToDTO(savedItem);

        return CommonApiResponse.builder()
                .isSuccess(true)
                .responseMessage("Thêm sản phẩm vào wishlist thành công")
                .build();
    }


    @Override
    public CommonApiResponse removeFromWishlist(int userId, int productId) {
        User user = userDAO.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productDAO.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        WishlistItem wishlistItem = wishlistItemDAO.findByUserAndProductAndDeletedFalse(user, product)
                .orElseThrow(() -> new RuntimeException("Wishlist item not found"));

        wishlistItem.setDeleted(true);
        wishlistItemDAO.save(wishlistItem);

        return CommonApiResponse.builder()
                .responseMessage("Xóa sản phẩm khỏi wishlist thành công")
                .isSuccess(true)
                .build();
    }


    @Override
    public List<WishlistItemDTO> getWishlistByUser(int userId) {
        User user = userDAO.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<WishlistItem> wishlistItems = wishlistItemDAO.findByUserAndDeletedFalse(user);

        return wishlistItems.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private WishlistItemDTO convertToDTO(WishlistItem item) {
        return WishlistItemDTO.builder()
                .id(item.getId())
                .userId(item.getUser().getId())
                .productId(item.getProduct().getId())
                .deleted(item.isDeleted())
                .product(ProductDTO.fromEntity(item.getProduct()))
                .build();
    }




}
