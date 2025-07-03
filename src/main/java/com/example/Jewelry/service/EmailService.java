package com.example.Jewelry.service;

public interface EmailService {
    void send(String to, String email);
    void sendOtpEmail(String email, String otp);
}
