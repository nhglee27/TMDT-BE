package com.example.Jewelry.service.ServiceImpl;


import com.example.Jewelry.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mail;
    private final static Logger LOG = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final Map<String, String> otpStorage = new HashMap<>();

    @Override
    @Async
    public void send(String to, String email) {
        try {

            MimeMessage mimeMessage = mail.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");

            mimeMessageHelper.setText(email, true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject("Confirm your email!");
            mimeMessageHelper.setFrom("demo.admin@demo.com");
            mail.send(mimeMessage);

        } catch (MessagingException e) {
            LOG.error("Failed to send mail", e);
            throw new IllegalStateException("Failed to send mail");
        }

    }

    @Override
    @Async
    public void sendOtpEmail(String email, String otp) {
        LOG.info("Sending OTP '{}' to email '{}'", otp, email);
        try {
            MimeMessage mimeMessage = mail.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText("Mã xác thực thanh toán của bạn là: <b>" + otp + "</b>", true);
            helper.setTo(email);
            helper.setSubject("Xác Nhận Mã OTP Đơn Hàng");
            helper.setFrom("demo.admin@demo.com");
            mail.send(mimeMessage);
        } catch (MessagingException e) {
            LOG.error("Gửi OTP thất bại", e);
            throw new IllegalStateException("Không thể gửi OTP");
        }
    }

    public String generateOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(email, otp);
        sendOtpEmail(email, otp);
        return otp;
    }

    public boolean verifyOtp(String email, String inputOtp) {
        return otpStorage.containsKey(email) && otpStorage.get(email).equals(inputOtp);
    }

    public void clearOtp(String email) {
        otpStorage.remove(email);
    }
}
