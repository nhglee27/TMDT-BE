package com.example.Jewelry.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Data
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String gender;

    private String firebaseUid;

    private String username;

    private String firstName;

    private String lastName;

    private String emailId;

    private String password;

    private String phoneNo;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    private LocalDateTime deletedAt;

    private String oauth2_id;

    private String oauth2_provider;

    private String role;

    private boolean email_verified;

    private String avatar;

    private BigDecimal amount;

    private String status;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Review> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryAddress> deliveryAddressList;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AuctionProduct> auctionProducts = new ArrayList<>();
}

