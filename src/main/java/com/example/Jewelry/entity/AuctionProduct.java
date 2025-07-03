package com.example.Jewelry.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Sản phẩm đấu giá, cái để hiển thị trên trang đấu giá
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuctionProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /** Liên kết đến sản phẩm được đăng ký đấu giá */
    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    /** CTV đăng ký làm đấu giá cho sản phẩm */
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author; // hoặc ctv entity nếu bạn tách riêng

    @ManyToOne
    @JoinColumn(name = "ctv_id")
    private CTV ctv;

    private Double budgetAuction;

    /** Đấu giá có đang hoạt động không */
    private String status;

    private int quantity;

    /** Thời điểm kết thúc đấu giá */
    private LocalDateTime auctionEndTime;
}
