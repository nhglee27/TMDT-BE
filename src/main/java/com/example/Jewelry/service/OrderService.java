package com.example.Jewelry.service;

import com.example.Jewelry.dto.OrderDTO;
import com.example.Jewelry.dto.request.OrderRequestDTO;
import com.example.Jewelry.dto.response.CommonAPIResForOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    CommonAPIResForOrder createOrder(OrderRequestDTO dto);
    Page<OrderDTO> getCurrentUserOrderHistory(Pageable pageable);
    OrderDTO getOrderDetailForCurrentUser(Integer orderId);
    OrderDTO cancelOrderForCurrentUser(Integer orderId);
}
