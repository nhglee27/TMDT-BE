package com.example.Jewelry.dto;

import com.example.Jewelry.dto.response.ImageDTO;
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
public class ProductDTO {

    private int id;
    private String name;
    private String description;
    private Double price;
    private String brand;
    private String size;
    private String productMaterial;
    private String occasion;
    private Double prevPrice;
    private Boolean productIsFavorite;
    private Boolean productIsCart;
    private String productIsBadge;
    private boolean deleted;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private LocalDateTime deletedAt;
    private List<ImageDTO> imageURLs;
    private List<MultipartFile> images;

    // Auction Product
    private AuctionProductDTO auctionProductDTO;

    // Category
    private int categoryId;
    private String categoryName;

    // Rating
    private double averageRating;
    private int totalRating;

    // CTV and Admin add
    private int userAddID;

    public static ProductDTO fromEntity(Product product) {
        if (product == null) return null;

        List<ImageDTO> imageDTOs = null;
        if (product.getImages() != null) {
            imageDTOs = product.getImages().stream()
                    .map(image -> new ImageDTO(image.getId(), image.getUrl()))
                    .toList();
        }
        AuctionProductDTO auctionProductDTO = null;
        if (product.getAuctionProduct() != null) {
            AuctionProduct auction = product.getAuctionProduct();
            auctionProductDTO = AuctionProductDTO.builder()
                    .id(auction.getId())
                    .auctionEndTime(auction.getAuctionEndTime())
                    .budgetAuction(auction.getBudgetAuction())
                    .quantity(auction.getQuantity())
                    .status(auction.getStatus())
                    .author_id(auction.getAuthor() != null ? auction.getAuthor().getId() : 0)
                    .collaboration_id(auction.getCtv() != null ? auction.getCtv().getId() : 0)
                    .build();
        }
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .status(product.getStatus())
                .prevPrice(product.getPrevPrice())
                .productIsBadge(product.getProductIsBadge())
                .imageURLs(imageDTOs)
                .auctionProductDTO(auctionProductDTO)
                .build();
    }

    public static Product toEntity(ProductDTO dto) {
        Product product = new Product();
        BeanUtils.copyProperties(dto, product, "id", "userAddID", "categoryId");
        return product;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }
}
