package com.example.Jewelry.dto.request;

import lombok.Data;

@Data
public class PromoteProductRequestDTO {
    private String name;
    private String description;
    private Double price;
    private int categoryId;
}