package com.example.Jewelry.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDTO {

    private int id;
    private String name;
    private MultipartFile thumbnail;
    private String status;
    private boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private LocalDateTime deletedAt;
}
