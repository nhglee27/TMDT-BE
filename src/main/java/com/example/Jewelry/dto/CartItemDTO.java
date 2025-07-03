package com.example.Jewelry.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private Integer productId;
    private int quantity;
}
