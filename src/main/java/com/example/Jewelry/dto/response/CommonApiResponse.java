package com.example.Jewelry.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonApiResponse {
    private String responseMessage;
    private boolean isSuccess;

    public static CommonApiResponse success(String message) {
        CommonApiResponse res = new CommonApiResponse();
        res.setSuccess(true);
        res.setResponseMessage(message);
        return res;
    }

    public static CommonApiResponse fail(String message) {
        CommonApiResponse res = new CommonApiResponse();
        res.setSuccess(false);
        res.setResponseMessage(message);
        return res;
    }

}

