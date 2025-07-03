package com.example.Jewelry.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.Jewelry.entity.Order.OrderStatus;

import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderDTO {

    private Integer id;
    private String ownerName;
    private String userEmail;
    private DeliveryAddressDTO deliveryAddress;
    private Double totalPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemDTO> items;
    private OrderPaymentDetailsDTO paymentDetails;


}
