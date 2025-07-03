package com.example.Jewelry.dto.response;

import lombok.Data;

@Data
public class ImageUploadResponse extends CommonApiResponse{
    private String imageURL;
}
