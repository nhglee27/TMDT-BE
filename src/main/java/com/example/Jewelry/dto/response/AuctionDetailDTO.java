package com.example.Jewelry.dto.response;

import com.example.Jewelry.dto.CtvInfoDTO;
import com.example.Jewelry.dto.CustomerInfoDTO;
import com.example.Jewelry.dto.ProductDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuctionDetailDTO {
    private ProductDTO productDetails;
    private CustomerInfoDTO customerInfo;
    private CtvInfoDTO ctvInfo;
}