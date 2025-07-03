package com.example.Jewelry.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UpdateAuctionDetailDTO {
    private String roomID;
    private String name;
    private String description;
    private String material;
    private String size;
    private String occasion;
    private Double budgetAuction; // Giá thỏa thuận
}