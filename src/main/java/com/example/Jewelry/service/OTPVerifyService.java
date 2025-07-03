package com.example.Jewelry.service;

import com.example.Jewelry.dto.request.OtpVerificationRequestDTO;
import com.example.Jewelry.dto.response.CommonApiResponse;

public interface OTPVerifyService {
    CommonApiResponse verifyOtp(OtpVerificationRequestDTO dto);
    CommonApiResponse resendOtp(Integer orderID);
}
