package com.example.Jewelry.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.example.Jewelry.Utility.Constant;

/**
 * Tin nhắn giữa các phiên đấu giá
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * Chỉ cần người gửi là biết phía nào để front end xử lí
     */
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private AuctionRoom auctionRoom;

    @Column(nullable = false)
    private String type = Constant.AuctionMessageType.MESSAGE.value();

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @PrePersist
    public void prePersist() {
        this.sentAt = LocalDateTime.now();
    }
}
