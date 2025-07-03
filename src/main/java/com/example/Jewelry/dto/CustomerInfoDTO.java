package com.example.Jewelry.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerInfoDTO {
    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNo;
}