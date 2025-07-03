package com.example.Jewelry.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CtvInfoDTO {
    private int ctvId;
    private int userId;
    private String name;
    private String email;
    private String phoneNo;
    private String location;
    private String experienceAndSkills;
}
