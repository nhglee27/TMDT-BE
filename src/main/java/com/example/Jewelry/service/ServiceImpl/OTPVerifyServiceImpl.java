package com.example.Jewelry.service.ServiceImpl;

import com.example.Jewelry.dao.OrderDAO;
import com.example.Jewelry.dto.request.OtpVerificationRequestDTO;
import com.example.Jewelry.dto.response.CommonApiResponse;
import com.example.Jewelry.entity.Order;
import com.example.Jewelry.entity.Payment;
import com.example.Jewelry.service.OTPVerifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OTPVerifyServiceImpl implements OTPVerifyService {
    private final OrderDAO orderRepo;
    private final EmailServiceImpl otpService;

    // Xac nhan don hang !
    public CommonApiResponse verifyOtp(OtpVerificationRequestDTO dto) {
        Order order = orderRepo.findById(dto.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order không tồn tại"));

        Payment payment = order.getPayment();
        if (payment == null || payment.getOtpCode() == null) {
            return CommonApiResponse.fail("Không tìm thấy mã OTP cho đơn hàng này");
        }

        if (!payment.getOtpCode().equals(dto.getOtpCode())) {
            return CommonApiResponse.fail("Mã OTP không chính xác");
        }

        if (payment.getOtpGeneratedAt().plusMinutes(5).isBefore(LocalDateTime.now())) {
            return CommonApiResponse.fail("Mã OTP đã hết hạn");
        }

        order.setStatus(Order.OrderStatus.CONFIRM);
        orderRepo.save(order);

        return CommonApiResponse.success("Xác thực OTP thành công, đơn hàng đã được xác nhận.");
    }

    public CommonApiResponse resendOtp(Integer orderId) {
        Optional<Order> optionalOrder = orderRepo.findById(orderId);
        if (optionalOrder.isEmpty()) {
            return CommonApiResponse.fail("Không tìm thấy đơn hàng với ID: " + orderId);
        }

        Order order = optionalOrder.get();
        Payment payment = order.getPayment();

        if (payment == null) {
            return CommonApiResponse.fail("Đơn hàng chưa có thông tin thanh toán");
        }

        String email = order.getUserEmail();
        if (email == null || email.isEmpty()) {
            return CommonApiResponse.fail("Email người dùng không hợp lệ");
        }

        String newOtp = otpService.generateOtp(email);
        payment.setOtpCode(newOtp);
        payment.setOtpGeneratedAt(LocalDateTime.now());

        orderRepo.save(order);

        return CommonApiResponse.success("Đã gửi lại mã OTP qua email.");
    }
}
