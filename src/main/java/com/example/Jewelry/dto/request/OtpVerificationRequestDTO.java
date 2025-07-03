package com.example.Jewelry.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpVerificationRequestDTO {
    private Integer orderId;
    private String otpCode;
}