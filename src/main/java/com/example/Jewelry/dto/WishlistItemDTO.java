package com.example.Jewelry.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishlistItemDTO {
    private int id;
    private int userId;
    private int productId;
    private boolean deleted;
    private ProductDTO product;
}


