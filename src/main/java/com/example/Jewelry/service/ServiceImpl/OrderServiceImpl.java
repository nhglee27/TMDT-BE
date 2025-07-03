package com.example.Jewelry.service.ServiceImpl;

import com.example.Jewelry.dao.OrderDAO;
import com.example.Jewelry.dto.*;
import com.example.Jewelry.dto.request.OrderRequestDTO;
import com.example.Jewelry.dto.response.CommonAPIResForOrder;
import com.example.Jewelry.entity.*;
import com.example.Jewelry.entity.Order.OrderStatus;
import com.example.Jewelry.dao.*;
import com.example.Jewelry.exception.BusinessException;
import com.example.Jewelry.exception.ResourceNotFoundException;
import com.example.Jewelry.service.OrderService;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderDAO orderRepo;
    private final ProductDAO productRepo;
    private final UserDAO userRepo;
    private final DeliveryAddressDAO deliveryAddressRepo;
    private final EmailServiceImpl otpService;
    private final CartItemDAO cartItemRepo;

    @Override
    @Transactional
    public CommonAPIResForOrder createOrder(OrderRequestDTO dto) {
        if (dto.getCartItems() == null || dto.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Giỏ hàng rỗng");
        }

        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User không tồn tại"));

        DeliveryAddress address = deliveryAddressRepo.findById(dto.getDeliveryAddressId())
                .orElseThrow(() -> new IllegalArgumentException("Địa chỉ giao hàng không tồn tại"));

        Order order = new Order();
        order.setUserEmail(user.getEmailId());
        order.setDeliveryAddress(address);
        order.setStatus(Order.OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> items = new ArrayList<>();
        List<CartItem> cartItemsToUpdate = new ArrayList<>();
        double total = 0;


        for (CartItemDTO itemDTO : dto.getCartItems()) {
            Product product = productRepo.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại"));

            List<CartItem> matchingCartItems = cartItemRepo.findAllByUserAndProductAndDeletedFalse(user, product);
            for (CartItem cartItem : matchingCartItems) {
                System.out.println(cartItem);
                cartItem.setDeleted(true);
                cartItemsToUpdate.add(cartItem);
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(product.getId());
            item.setPrice(product.getPrice());
            item.setQuantity(itemDTO.getQuantity());

            total += product.getPrice() * itemDTO.getQuantity();
            items.add(item);
        }

        double discount = dto.getDiscount() != null ? dto.getDiscount() : 0.0;
        order.setItems(items);
        order.setTotalPrice(Math.max(0, total - discount));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentStatus(Payment.PaymentStatus.PENDING);
        payment.setPaymentMethod(dto.getPaymentMethod().equals("online") ? Payment.PaymentMethod.online : Payment.PaymentMethod.COD);
        payment.setOtpCode(otpService.generateOtp(user.getEmailId()));
        payment.setOtpGeneratedAt(LocalDateTime.now());

        order.setPayment(payment);

        orderRepo.save(order);
        cartItemRepo.saveAll(cartItemsToUpdate);

        return CommonAPIResForOrder.success("Tạo đơn hàng thành công, vui lòng kiểm tra email để xác thực thanh toán bằng OTP.", order.getId());
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new com.example.Jewelry.exception.BusinessException("Người dùng chưa được xác thực hoặc phiên làm việc không hợp lệ.");
        }

        Object principal = authentication.getPrincipal();
        String userEmail = null;

        if (principal instanceof UserDetails) {
            userEmail = ((UserDetails) principal).getUsername();
        } else if (principal instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) principal;
            userEmail = oAuth2User.getAttribute("email");
            if (userEmail == null) {
                userEmail = oAuth2User.getName();
            }
        } else {
            throw new com.example.Jewelry.exception.BusinessException("Không thể xác định email người dùng từ thông tin xác thực không xác định.");
        }

        if (userEmail == null || userEmail.isEmpty()) {
            throw new com.example.Jewelry.exception.BusinessException("Không thể lấy được email người dùng từ thông tin xác thực.");
        }
        return userEmail;
    }

    private OrderDTO mapToOrderListDTO(Order order) {
        if (order == null) {
            return null;
        }
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setStatus(order.getStatus());
        // trả về OderDTO với các trường còn lại là null
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getCurrentUserOrderHistory(Pageable pageable) {
        String userEmail = getCurrentUserEmail();
        //kiểm tra người dùng trong db
        User user = userRepo.findByEmailId(userEmail);
        if (user == null) {
            throw new com.example.Jewelry.exception.ResourceNotFoundException("Người dùng với email " + userEmail + " không tìm thấy.");
        }

        Page<Order> orderPage = orderRepo.findByUserEmailOrderByCreatedAtDesc(userEmail, pageable);
        //đổi order thành orderDTO
        List<OrderDTO> orderDTOs = orderPage.getContent().stream()
                .map(this::mapToOrderListDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(orderDTOs, pageable, orderPage.getTotalElements());
    }
    private OrderItemDTO mapToOrderItemDTO(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        Product product = null;
        if (orderItem.getProductId() != null) {
            product = productRepo.findById(orderItem.getProductId()).orElse(null);
        }
        OrderItemDTO rs =OrderItemDTO.convertItem(orderItem);
                rs.setProductName(product != null ? product.getName() : "Sản phẩm không tồn tại");
        return rs;
    }
    private OrderPaymentDetailsDTO mapToOrderPaymentDetailsDTO(Payment payment) {
        if (payment == null) {
            return null;
        }
        return OrderPaymentDetailsDTO.builder()
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .paymentDate(payment.getPaymentDate())
                .transactionId(payment.getTransactionId())
                .build();
    }

    private OrderDTO mapToOrderDetailDTO(Order order, User user) {
        if (order == null) {
            return null;
        }

        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(this::mapToOrderItemDTO)
                .collect(Collectors.toList());

        DeliveryAddressDTO deliveryAddressDTO = null;
        if (order.getDeliveryAddress() != null) {
            deliveryAddressDTO = DeliveryAddressDTO.convertDeliveryAddress(order.getDeliveryAddress());
        }

        OrderPaymentDetailsDTO paymentDetailsDTO = null;
        if (order.getPayment() != null) {
            paymentDetailsDTO = mapToOrderPaymentDetailsDTO(order.getPayment());
        }

        String ownerName = "";
        if (user != null) {
            ownerName = (user.getFirstName() != null ? user.getFirstName() : "") +
                    " " +
                    (user.getLastName() != null ? user.getLastName() : "");
            ownerName = ownerName.trim();
        }


        return OrderDTO.builder()
                .id(order.getId())
                .ownerName(ownerName)
                .userEmail(order.getUserEmail())
                .deliveryAddress(deliveryAddressDTO)
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .items(itemDTOs)
                .paymentDetails(paymentDetailsDTO)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderDetailForCurrentUser(Integer orderId) {
        String userEmail = getCurrentUserEmail();
        User user = userRepo.findByEmailId(userEmail);
        if (user == null) {
            throw new ResourceNotFoundException("Người dùng với email " + userEmail + " không tìm thấy.");
        }

        Order order = orderRepo.findByIdAndUserEmail(orderId, userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Đơn hàng với ID " + orderId + " không tồn tại hoặc không thuộc về bạn."));

        return mapToOrderDetailDTO(order, user);
    }

    @Override
    @Transactional
    public OrderDTO cancelOrderForCurrentUser(Integer orderId) {
        String userEmail = getCurrentUserEmail();
        User user = userRepo.findByEmailId(userEmail);
        if (user == null) {
            throw new ResourceNotFoundException("Người dùng với email " + userEmail + " không tìm thấy.");
        }

        Order order = orderRepo.findByIdAndUserEmail(orderId, userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Đơn hàng với ID " + orderId + " không tồn tại hoặc không thuộc về bạn."));

        if (order.getStatus() != Order.OrderStatus.PENDING && order.getStatus() != Order.OrderStatus.CONFIRM) {
            throw new BusinessException("Đơn hàng không thể hủy ở trạng thái hiện tại: " + order.getStatus());
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        if (order.getPayment() != null) {
            order.getPayment().setPaymentStatus(Payment.PaymentStatus.CANCELLED);
        }

        Order updatedOrder = orderRepo.save(order);
        return mapToOrderDetailDTO(updatedOrder, user);
    }

    public List<Order> getAllOrders() {
        String userEmail = getCurrentUserEmail();
        User user = userRepo.findByEmailId(userEmail);
        if (user == null) {
            throw new ResourceNotFoundException("Người dùng với email " + userEmail + " không tìm thấy.");
        }
        if (user.getRole() == null || !user.getRole().equalsIgnoreCase("admin")) {
            throw new ResourceNotFoundException("Người dùng với email " + userEmail + " không phải là admin.");
        }
        return orderRepo.findAll();
    }

    public Order getSingleOrder(int orderID) {
        String userEmail = getCurrentUserEmail();
        User user = userRepo.findByEmailId(userEmail);
        if (user == null) {
            throw new ResourceNotFoundException("Người dùng với email " + userEmail + " không tìm thấy.");
        }
        if (user.getRole() == null || !user.getRole().equalsIgnoreCase("admin")) {
            throw new ResourceNotFoundException("Người dùng với email " + userEmail + " không phải là admin.");
        }
        return orderRepo.findById(orderID).orElse(null);
    }

    public boolean updateOrderStatus(Order singleOrder, OrderStatus newStatus) {
        String userEmail = getCurrentUserEmail();
        User user = userRepo.findByEmailId(userEmail);
        if (user == null) {
            throw new ResourceNotFoundException("Người dùng với email " + userEmail + " không tìm thấy.");
        }
        if (user.getRole() == null || !user.getRole().equalsIgnoreCase("admin")) {
            throw new ResourceNotFoundException("Người dùng với email " + userEmail + " không phải là admin.");
        }
        OrderStatus oldStatus = singleOrder.getStatus();
        // you can not update a cancelled or confirmed order
        if (oldStatus == OrderStatus.CANCELLED || oldStatus == OrderStatus.CONFIRM)
            return false;
        // you can not update to the same value
        if (oldStatus == newStatus)
            return false;
        // you can not update the unpaid order?
        singleOrder.setStatus(newStatus);
        orderRepo.save(singleOrder);
        return true;
    }




    public CommonAPIResForOrder getOrderStatus(Integer orderId) {
        Optional<Order> orderOpt = orderRepo.findById(orderId);
        if (orderOpt.isEmpty()) {
            return CommonAPIResForOrder.fail("Không tìm thấy đơn hàng", orderId);
        }

        Order order = orderOpt.get();
        boolean isPaid = order.getPayment().getPaymentStatus() == Payment.PaymentStatus.PAID;

        if (isPaid) {
            return CommonAPIResForOrder.success("Đơn hàng đã được thanh toán", orderId);
        } else {
            return CommonAPIResForOrder.fail("Đơn hàng chưa được thanh toán", orderId);
        }
    }

}
