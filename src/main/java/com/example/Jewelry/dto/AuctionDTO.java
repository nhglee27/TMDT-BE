package com.example.Jewelry.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Getter @Setter
public class AuctionDTO {
    private String description;
    private String budget;
    private List<String> images;
    private String jewelryType;
    private String material;
    private String size;
    private String specialRequest;
    private LocalDate deadline;
}

