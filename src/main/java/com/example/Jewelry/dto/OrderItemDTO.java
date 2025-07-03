package com.example.Jewelry.dto;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.Jewelry.controller.ProductController;
import com.example.Jewelry.dao.ProductDAO;
import com.example.Jewelry.entity.Image;
import com.example.Jewelry.entity.OrderItem;
import com.example.Jewelry.entity.Product;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemDTO {
    
    private Integer productId;

    private String productName;

    private String productImageUrl;

    private Integer quantity;

    private Double price;

    public static List<OrderItemDTO> convertItemList(List<OrderItem> items) {
        return items.stream().map((item) -> convertItem(item)).toList();
    }

    public static OrderItemDTO convertItem(OrderItem item) {

        OrderItemDTO result = new OrderItemDTO();
        result.setProductId(item.getProductId());
        
        result.setPrice(item.getPrice());
        result.setQuantity(item.getQuantity());

        return result;

    }
}
