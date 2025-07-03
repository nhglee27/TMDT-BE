package com.example.Jewelry.dto;

import lombok.Data;

@Data
public class ChatMessageDTO {

    private int id;
    private String type;
    private String content;
    private Integer senderId; // Map to User.id
    private String roomId; // Map to User.id
    private String sentAt;
}
