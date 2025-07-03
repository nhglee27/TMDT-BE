package com.example.Jewelry.dto;

import com.example.Jewelry.entity.Payment.PaymentMethod;
import com.example.Jewelry.entity.Payment.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPaymentDetailsDTO {
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;
    private String transactionId;
}