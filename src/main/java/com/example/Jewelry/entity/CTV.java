package com.example.Jewelry.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CTV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    private String phoneNo;
    private String location;
    private String experienceAndSkills;
    private String sampleWorkLink;
    private String reason;
    private LocalDateTime createdAt;
    private String status;

    /** Một CTV có nhiều AuctionProduct */
    @OneToMany(mappedBy = "ctv", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AuctionProduct> auctionProducts = new ArrayList<>();
}

