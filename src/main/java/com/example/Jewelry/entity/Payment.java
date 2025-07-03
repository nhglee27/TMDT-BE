package com.example.Jewelry.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public enum PaymentMethod {
        online, COD
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    public enum PaymentStatus {
        PENDING, PAID, CANCELLED
    }

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String transactionId;

    private String otpCode;

    private LocalDateTime otpGeneratedAt;

    private LocalDateTime paymentDate;

    @PrePersist
    public void prePersist() {
        this.otpGeneratedAt = LocalDateTime.now();
    }
}


