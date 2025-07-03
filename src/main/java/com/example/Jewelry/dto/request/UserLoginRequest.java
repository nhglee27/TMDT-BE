package com.example.Jewelry.dto.request;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String emailId;
    private String password;
}

