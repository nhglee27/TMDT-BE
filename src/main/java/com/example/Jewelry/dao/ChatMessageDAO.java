package com.example.Jewelry.dao;

import com.example.Jewelry.entity.AuctionRoom;
import com.example.Jewelry.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageDAO extends JpaRepository<ChatMessage, Integer> {

    List<ChatMessage> findByAuctionRoom(AuctionRoom auctionRoom);
    // List<ChatMessage> findByProductIdAndSenderIdAndRecipientIdOrRecipientIdAndSenderId(
    // int productId, int senderId, int recipientId, int recipientId2, int senderId2);
}