package com.example.Jewelry.controller;

import com.example.Jewelry.dao.ProductDAO;
import com.example.Jewelry.dao.UserDAO;
import com.example.Jewelry.dto.DeliveryAddressDTO;
import com.example.Jewelry.dto.OrderDTO;
import com.example.Jewelry.dto.OrderItemDTO;
import com.example.Jewelry.dto.OrderPaymentDetailsDTO;
import com.example.Jewelry.dto.PaymentDTO;
import com.example.Jewelry.dto.request.OrderRequestDTO;
import com.example.Jewelry.dto.response.CommonAPIResForOrder;
import com.example.Jewelry.dto.response.OrderDTOResponse;
import com.example.Jewelry.entity.Image;
import com.example.Jewelry.entity.Order;
import com.example.Jewelry.entity.Payment;
import com.example.Jewelry.entity.Order.OrderStatus;
import com.example.Jewelry.entity.Product;
import com.example.Jewelry.entity.User;
import com.example.Jewelry.exception.ResourceNotFoundException;
import com.example.Jewelry.resource.ProductResource;
import com.example.Jewelry.service.ProductService;
import com.example.Jewelry.service.UserService;
import com.example.Jewelry.service.ServiceImpl.OrderServiceImpl;
import com.example.Jewelry.exception.BusinessException;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    private final OrderServiceImpl orderService;
    private final UserDAO userDao;
    private final UserService userService;
    private final ProductService productService;
//  this is
//    {
//        "userId": 3,
//            "deliveryAddressId": 1,
//            "cartItems": [
//        {
//            "productId": 2,
//                "quantity": 2
//        },
//        {
//            "productId": 1,
//                "quantity": 1
//        }
//  ],
//        "totalAmount": 1000.0,
//            "discount": 50.0,
//            "paymentMethod": true
//    }


    @PostMapping("/create")
    public ResponseEntity<CommonAPIResForOrder> createOrder(@RequestBody OrderRequestDTO dto) {
        CommonAPIResForOrder response = orderService.createOrder(dto);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/fetch/all")
    public ResponseEntity<OrderDTOResponse> getAllOrderDTOs() {
        List<Order> list = orderService.getAllOrders();
        List<OrderDTO> uh = list.stream().map((o) -> {
            OrderDTO result = new OrderDTO();
            User user = userDao.findByEmailId(o.getUserEmail());
            BeanUtils.copyProperties(o, result);
            result.setDeliveryAddress(DeliveryAddressDTO.convertDeliveryAddress(o.getDeliveryAddress()));
            result.setOwnerName(user.getLastName() + " " + user.getFirstName());
            return result;
        }).toList();
        OrderDTOResponse response = new OrderDTOResponse();
        response.setSuccess(true);
        response.setResponseMessage("Yipee");
        response.setOrderDTOList(uh);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/fetch/detail/{orderID}")
    public ResponseEntity<OrderDTOResponse> getSingleOrderDetail(@PathVariable int orderID) {
        Order singleOrder = orderService.getSingleOrder(orderID);
        OrderDTO result = new OrderDTO();
        User user = userService.getUserByEmailid(singleOrder.getUserEmail());
        BeanUtils.copyProperties(singleOrder, result);
        /** Payment DTO */
        Payment payment = singleOrder.getPayment();
        OrderPaymentDetailsDTO pDto = new OrderPaymentDetailsDTO();
        pDto.setTransactionId(payment.getTransactionId());
        pDto.setPaymentDate(payment.getPaymentDate());
        pDto.setPaymentMethod(payment.getPaymentMethod());
        pDto.setPaymentStatus(payment.getPaymentStatus());
        // Payment
        result.setPaymentDetails(pDto);
        result.setDeliveryAddress(DeliveryAddressDTO.convertDeliveryAddress(singleOrder.getDeliveryAddress()));
            List<OrderItemDTO> itemLists = OrderItemDTO.convertItemList(singleOrder.getItems());
            itemLists.forEach((item) -> {
                Product product = productService.getById(item.getProductId());
                Image pImages = product.getImages() != null ? product.getImages().get(0) : null;
                item.setProductName(product.getName());
                item.setProductImageUrl(pImages == null ? null : pImages.getUrl());
                item.setProductId(product.getId());
            });
            result.setItems(itemLists);
        result.setOwnerName(user.getLastName() + " " + user.getFirstName());
        OrderDTOResponse response = new OrderDTOResponse();
        response.setSuccess(true);
        response.setResponseMessage("Yipee");
        response.setOrder(result);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/update/{orderID}")
    public ResponseEntity<OrderDTOResponse> updateOrderStatus(@PathVariable int orderID, @RequestParam OrderStatus status) {
        OrderDTOResponse response = new OrderDTOResponse();
        // nếu ko lấy được, thì báo lỗi thất bại
        Order singleOrder = orderService.getSingleOrder(orderID);
        if (singleOrder == null) {
            response.setSuccess(false);
            response.setResponseMessage("Could not get the order to set status to");
        }
        else {
            boolean state = orderService.updateOrderStatus(singleOrder, status);
            response.setSuccess(state);
            response.setResponseMessage(state ? "Cập nhật thành công" : "Cập nhật không thành công");
        }

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserOrderHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {

        Pageable pageable;
        try {
            Sort.Direction direction = Sort.Direction.fromString(sort.length > 1 ? sort[1].toUpperCase() : "DESC");
            Sort sortOrder = Sort.by(direction, sort[0]);
            pageable = PageRequest.of(page, size, sortOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Tham số sắp xếp không hợp lệ. Ví dụ: 'createdAt,desc' hoặc 'totalPrice,asc'.");
        }

        try {
            Page<OrderDTO> orderHistoryPage = orderService.getCurrentUserOrderHistory(pageable);
            return ResponseEntity.ok(orderHistoryPage);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã có lỗi xảy ra, vui lòng thử lại sau.");
        }
    }

    @GetMapping("/me/{orderId}")
    public ResponseEntity<?> getCurrentUserOrderDetail(@PathVariable Integer orderId) {
        try {
            OrderDTO orderDetail = orderService.getOrderDetailForCurrentUser(orderId);
            return ResponseEntity.ok(orderDetail);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã có lỗi xảy ra khi xử lý yêu cầu của bạn.");
        }
    }

    @PostMapping("/me/{orderId}/cancel")
    public ResponseEntity<?> cancelCurrentUserOrder(@PathVariable Integer orderId) {
        try {
            OrderDTO cancelledOrder = orderService.cancelOrderForCurrentUser(orderId);
            return ResponseEntity.ok(cancelledOrder);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã có lỗi xảy ra khi xử lý yêu cầu của bạn.");
        }
    }
    @GetMapping("/status/{orderId}")
    public ResponseEntity<CommonAPIResForOrder> getOrderStatus(@PathVariable Integer orderId) {
        CommonAPIResForOrder response = orderService.getOrderStatus(orderId);
        return ResponseEntity.ok(response);
    }



}
