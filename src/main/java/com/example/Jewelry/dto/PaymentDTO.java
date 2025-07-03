package com.example.Jewelry.dto;
import lombok.*;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Long orderId;
    private String method;
    private String otp;
}

