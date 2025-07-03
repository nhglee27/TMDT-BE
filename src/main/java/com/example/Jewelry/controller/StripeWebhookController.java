package com.example.Jewelry.controller;

import com.example.Jewelry.config.StripeConfig;
import com.example.Jewelry.dao.OrderDAO;
import com.example.Jewelry.entity.Order;
import com.example.Jewelry.entity.Payment;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/webhook")
@CrossOrigin(origins = "http://localhost:3000")
public class StripeWebhookController {

    private final StripeConfig stripeConfig;
    private final OrderDAO orderRepo;

    @Autowired
    public StripeWebhookController(StripeConfig stripeConfig, OrderDAO orderRepo) {
        this.stripeConfig = stripeConfig;
        this.orderRepo = orderRepo;
    }

    @PostMapping("/stripe")
    public String handleStripeWebhook(HttpServletRequest request) throws IOException {
        byte[] payloadBytes = request.getInputStream().readAllBytes();
        String payload = new String(payloadBytes, StandardCharsets.UTF_8);

        String sigHeader = request.getHeader("Stripe-Signature");
        String endpointSecret = stripeConfig.getEndpointSecret();

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            System.out.println("❌ Invalid signature: " + e.getMessage());
            return "Invalid signature";
        }

        System.out.println("✅ Received Stripe event: " + event.getType());

        switch (event.getType()) {
            case "checkout.session.completed" -> handleCheckoutSessionCompleted(event);
//            case "charge.succeeded" -> handleChargeSucceeded(event);
//            case "charge.failed" -> handleChargeFailed(event);
//            case "payment_intent.succeeded" -> handlePaymentIntentSucceeded(event);
//            case "payment_intent.payment_failed" -> handlePaymentIntentFailed(event);
            default -> {
                System.out.println("⚠️ Unhandled event type: " + event.getType());
                return "Unhandled event type: " + event.getType();
            }
        }


        return "OK";
    }


    private void handleCheckoutSessionCompleted(Event event) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(event.getData().getObject().toJson());

            String sessionId = rootNode.get("id").asText();
            JsonNode metadataNode = rootNode.get("metadata");

            if (metadataNode != null && metadataNode.has("orderId")) {
                String orderIdStr = metadataNode.get("orderId").asText();
                String amountStr = metadataNode.has("amount") ? metadataNode.get("amount").asText() : null;

                System.out.println("🎯 Session ID: " + sessionId);
                System.out.println("📦 Metadata - orderId: " + orderIdStr + ", amount: " + amountStr);

                int orderId = Integer.parseInt(orderIdStr);
                Order order = orderRepo.findById(orderId).orElse(null);
                if (order != null && order.getPayment() != null) {
                    order.getPayment().setPaymentStatus(Payment.PaymentStatus.PAID);
                    order.getPayment().setPaymentDate(LocalDateTime.now());
                    orderRepo.save(order);
                    System.out.println("✅ Đã cập nhật trạng thái thanh toán cho đơn hàng " + orderId);
                } else {
                    System.out.println("⚠️ Không tìm thấy đơn hàng hoặc payment null.");
                }
            } else {
                System.out.println("⚠️ Metadata không có orderId.");
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi xử lý webhook: " + e.getMessage());
            e.printStackTrace();
        }
    }



//    private void handleChargeSucceeded(Event event) {
//
//    }

//    private void handleChargeFailed(Event event) {
//    }

//    private void handlePaymentIntentSucceeded(Event event) {
//
//    }

//    private void handlePaymentIntentFailed(Event event) {
//
//    }


}
