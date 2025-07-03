package com.example.Jewelry.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    /** Product ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /** Product Name */
    private String name;

    /** Product Description */
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    /** Product Category it belongs too */
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "category_id")
    private Category category;

    /** Product Brands? */
    @Column
    private String brand;

    /** Product Images to show */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    /** Product Sizing  */
    @Column
    private String size;

    /** Product material */
    @Column(name = "material")
    private String productMaterial;

    /** Product for which occasion */
    @Column
    private String occasion;

    /** Product original price */
    @Column(name = "prev_price")
    private Double prevPrice;

    @Column(name = "product_is_favorite")
    private Boolean productIsFavorite;

    @Column(name = "product_is_cart")
    private Boolean productIsCart;

    /** Tag */
    @Column(name = "product_is_badge")
    private String productIsBadge;

    @OneToMany(mappedBy = "productId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Review> reviews;

    private boolean deleted;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    private LocalDateTime deletedAt;
    @JsonBackReference
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private AuctionProduct auctionProduct;
}
