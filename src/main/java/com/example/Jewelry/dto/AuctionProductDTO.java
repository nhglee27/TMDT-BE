package com.example.Jewelry.dto;

import com.example.Jewelry.entity.Product;
import com.example.Jewelry.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuctionProductDTO {
    private int id;

    // Author created auction ID
    private int author_id;

    // I need named
    private String authorName;

    // CIV Collaboration ID
    private int collaboration_id;

    private Double budgetAuction;

    /** Đấu giá có đang hoạt động không */
    private String status;

    // Số luợng
    private int quantity;

    /** Thời điểm kết thúc đấu giá */
    private LocalDateTime auctionEndTime;
}
