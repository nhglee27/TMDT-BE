package com.example.Jewelry.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCTVRequest {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNo;
    private String location;
    private String experienceAndSkills;
    private String sampleWorkLink;
    private String reason;

}
