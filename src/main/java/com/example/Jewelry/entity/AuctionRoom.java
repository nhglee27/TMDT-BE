package com.example.Jewelry.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.Jewelry.Utility.Constant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Phòng Chat giữa mỗi khách hàng với CTV trong một phiên đấu giá
 * Đây cũng là nơi lưu Auction Request
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuctionRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name="auction_id")
    private AuctionProduct currentAuction;

    /**
     * Thằng muốn join (1-1)
     */
    @ManyToOne
    @JoinColumn(name="ctv_id")
    private CTV collaborator;

    /** 
     * Giá đề xuất từ phía đăng kí
     */
    private Double proposingPrice;

    /**
     * Trạng thái chấp nhận từ bên owner để chốt
     */
    private String status = Constant.CtvStatus.PENDING.value();

    /**
     * Trạng thái chấp nhận từ bên ctv để chốt (mượn trường constant.ctvstatus)
     */
    private String statusCTV = Constant.CtvStatus.PENDING.value();

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }


}
