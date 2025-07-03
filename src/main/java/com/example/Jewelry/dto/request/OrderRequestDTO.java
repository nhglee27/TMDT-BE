package com.example.Jewelry.dto.request;

import com.example.Jewelry.dto.CartItemDTO;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    private Integer userId;
    private List<CartItemDTO> cartItems;
    private Integer deliveryAddressId;
    private Double totalAmount;
    private Double discount;
    private String paymentMethod;

}
