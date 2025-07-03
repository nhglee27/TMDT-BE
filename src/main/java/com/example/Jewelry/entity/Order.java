package com.example.Jewelry.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String userEmail;

    @ManyToOne
    @JoinColumn(name = "delivery_address_id")
    private DeliveryAddress deliveryAddress;

    private Double totalPrice;

    public enum OrderStatus {
        PENDING, CONFIRM, CANCELLED, PAID
    }

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}
