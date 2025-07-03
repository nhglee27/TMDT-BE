package com.example.Jewelry.dto.request;

import lombok.Data;

@Data
public class ReviewRequest {
    private int productId;
    private int rating;
    private String comment;
}
