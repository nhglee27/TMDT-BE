package com.example.Jewelry.controller;

import com.example.Jewelry.dao.OrderDAO;
import com.example.Jewelry.entity.Order;
import com.example.Jewelry.entity.Payment;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:3000")
public class StripePaymentController {

    @Autowired
    private OrderDAO orderRepo;

    @PostMapping("/create-stripe-session")
    public ResponseEntity<?> createStripeSession(@RequestBody Map<String, Integer> payload) throws StripeException {
        Integer orderId = payload.get("id");

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Đơn hàng không tồn tại"));

        if (order.getPayment().getPaymentStatus() == Payment.PaymentStatus.PAID) {
            return ResponseEntity.badRequest().body("Đơn hàng đã được thanh toán.");
        }

        double exchangeRate = 26000.0; // Tỷ giá USD/VND
        double amountInUSD = order.getTotalPrice() / exchangeRate;
        long amountInCents = Math.round(amountInUSD * 100); // Stripe yêu cầu đơn vị cents


        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment")
                .setCancelUrl("https://yourdomain.com/payment-cancel?orderId=" + order.getId())
                .putMetadata("orderId", String.valueOf(order.getId()))
                .putMetadata("amount", String.valueOf(amountInCents))
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(amountInCents)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Thanh toán đơn hàng #" + order.getId())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        Session session = Session.create(params);

        return ResponseEntity.ok(Map.of("checkoutUrl", session.getUrl()));
    }

}
