package com.example.Jewelry.controller;

import com.example.Jewelry.Utility.Constant;
import com.example.Jewelry.dao.AuctionProductDAO;
import com.example.Jewelry.dao.AuctionRoomDAO;
import com.example.Jewelry.dao.ChatMessageDAO;
import com.example.Jewelry.dao.ProductDAO;
import com.example.Jewelry.dao.UserDAO;
import com.example.Jewelry.dto.ChatMessageDTO;
import com.example.Jewelry.entity.AuctionRoom;
import com.example.Jewelry.entity.ChatMessage;
import com.example.Jewelry.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatMessageDAO chatMessageDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private AuctionProductDAO auctionProductDAO;

    @Autowired
    private AuctionRoomDAO auctionRoomDAO;

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload ChatMessageDTO chatMessageDTO) {
        System.out.println("HEY");
        // Validate sender and product
        User sender = userDAO.findById(chatMessageDTO.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid sender ID"));

        AuctionRoom auctionRoom = auctionRoomDAO.findById(UUID.fromString(chatMessageDTO.getRoomId())).orElseThrow(
                () -> new IllegalArgumentException("Auction not found"));

        // Save message to database
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(chatMessageDTO.getType());
        chatMessage.setSender(sender);
        chatMessage.setAuctionRoom(auctionRoom);

        // Edge cases: ACCEPT or REJECT or PENDING
        if (chatMessageDTO.getType().equals(Constant.AuctionMessageType.ACCEPT.value()) ||
                chatMessageDTO.getType().equals(Constant.AuctionMessageType.REJECT.value()) ||
                chatMessageDTO.getType().equals(Constant.AuctionMessageType.PENDING.value())) {
            if (chatMessageDTO.getType().equals(Constant.AuctionMessageType.ACCEPT.value())) {
                // xử lí accept đợi
                if (sender.getId() == auctionRoom.getCollaborator().getUser().getId())
                    auctionRoom.setStatusCTV(Constant.CtvStatus.APPROVED.value());
                else if (sender.getId() == auctionRoom.getCurrentAuction().getAuthor().getId())
                    auctionRoom.setStatus(Constant.CtvStatus.APPROVED.value());
                chatMessage.setContent("%s sẵn sàng chấp nhận.".formatted(sender.getFirstName()));

            } else if (chatMessageDTO.getType().equals(Constant.AuctionMessageType.REJECT.value())) {
                // xử lí hủy
                if (sender.getId() == auctionRoom.getCollaborator().getUser().getId())
                    auctionRoom.setStatusCTV(Constant.CtvStatus.REJECTED.value());
                else if (sender.getId() == auctionRoom.getCurrentAuction().getAuthor().getId())
                    auctionRoom.setStatus(Constant.CtvStatus.REJECTED.value());
                chatMessage.setContent("%s muốn từ chối chấp nhận.".formatted(sender.getFirstName()));
            } else if (chatMessageDTO.getType().equals(Constant.AuctionMessageType.PENDING.value())) {
                // xử lí hủy
                if (sender.getId() == auctionRoom.getCollaborator().getUser().getId())
                    auctionRoom.setStatusCTV(Constant.CtvStatus.PENDING.value());
                else if (sender.getId() == auctionRoom.getCurrentAuction().getAuthor().getId())
                    auctionRoom.setStatus(Constant.CtvStatus.PENDING.value());
                chatMessage.setContent("%s còn do dự.".formatted(sender.getFirstName()));
            }
            auctionRoom = auctionRoomDAO.save(auctionRoom);

            if (auctionRoom.getStatus().equals(auctionRoom.getStatusCTV())) {
                if (auctionRoom.getStatus().equals(Constant.CtvStatus.APPROVED.value())) {
                    auctionRoom.getCurrentAuction().setStatus(Constant.ActiveStatus.DEACTIVATED.value());
                    auctionRoom.getCurrentAuction().getProduct().setStatus(Constant.ActiveStatus.DEACTIVATED.value());

                    productDAO.save(auctionRoom.getCurrentAuction().getProduct());
                    auctionProductDAO.save(auctionRoom.getCurrentAuction());
                    chatMessage.setContent(chatMessage.getContent() + "\n" + "Chốt đấu giá này xong!");
                }
            }
        } else {
            chatMessage.setContent(chatMessageDTO.getContent());
        }
        chatMessage = chatMessageDAO.save(chatMessage);

        // Convert to DTO for WebSocket
        chatMessageDTO.setId(chatMessage.getId());
        chatMessageDTO.setSentAt(chatMessage.getSentAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        chatMessageDTO.setContent(chatMessage.getContent());

        // Send to recipient's topic
        String destination = String.format("/topic/reverse-auction/%s", chatMessageDTO.getRoomId());
        messagingTemplate.convertAndSend(destination, chatMessageDTO);
    }

    @GetMapping("/history")
    public List<ChatMessageDTO> getChatHistory(@RequestParam String roomID) {
        AuctionRoom auctionRoom = auctionRoomDAO.findById(UUID.fromString(roomID)).orElseThrow(
                () -> new IllegalArgumentException("Auction not found"));

        List<ChatMessage> messages = chatMessageDAO.findByAuctionRoom(auctionRoom);
        return messages.stream().map(msg -> {
            ChatMessageDTO dto = new ChatMessageDTO();
            dto.setId(msg.getId());
            dto.setContent(msg.getContent());
            dto.setSenderId(msg.getSender().getId());
            dto.setRoomId(msg.getAuctionRoom().getId().toString());
            dto.setSentAt(msg.getSentAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            return dto;
        }).collect(Collectors.toList());
    }
}
