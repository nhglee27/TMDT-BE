package com.example.Jewelry.dto.response;

import com.example.Jewelry.entity.Image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDTO {
    private int id;
    private String url;

    public static ImageDTO fromEntity(Image image) {
        if (image == null)
            return null;
        ImageDTO dto = new ImageDTO();
        dto.setId(image.getId());
        dto.setUrl(image.getUrl());
        return dto;
    }
}
