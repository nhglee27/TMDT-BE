package com.example.Jewelry.dto.request;

import com.example.Jewelry.entity.Product;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddProductRequestDTO {

    private int id;
    private String name;
    private String description;
    private Double price;
    private String brand;
    private List<MultipartFile> images;
    private String size;
    private String material;
    private String color;
    private String occasion;
    private Double prevPrice;
    private Double budgetAuction;
    private String productIsBadge;
    private String status;
    private int quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private LocalDateTime deletedAt;
    private LocalDateTime auctionEndAt;
    // Category
    private int categoryId;
    // CTV and Admin add
    private int userAddID;

    public static Product toEntity(AddProductRequestDTO dto) {
        Product product = new Product();
        BeanUtils.copyProperties(dto, product, "id", "userAddID", "categoryId");
        return product;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }



}
