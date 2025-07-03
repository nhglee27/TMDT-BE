package com.example.Jewelry.dto.response;

import java.util.List;

import com.example.Jewelry.dto.OrderDTO;
import com.example.Jewelry.entity.Order;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDTOResponse extends CommonAPIResForOrder {

    private List<OrderDTO> orderDTOList;
    private List<Order> orderList;
    private OrderDTO order;
    

}
