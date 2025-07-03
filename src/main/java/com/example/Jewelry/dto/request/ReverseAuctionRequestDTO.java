package com.example.Jewelry.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReverseAuctionRequestDTO {

    private int ctvID;
    private int auctionID;
    private Double proposedPrice;

}
