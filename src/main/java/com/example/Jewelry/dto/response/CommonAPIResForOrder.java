package com.example.Jewelry.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonAPIResForOrder {
    private String responseMessage;
    private boolean isSuccess;
    private Integer orderID;

    public static CommonAPIResForOrder success(String message, Integer orderID) {
        CommonAPIResForOrder res = new CommonAPIResForOrder();
        res.setSuccess(true);
        res.setResponseMessage(message);
        res.setOrderID(orderID);
        return res;
    }

    public static CommonAPIResForOrder fail(String message, Integer orderID) {
        CommonAPIResForOrder res = new CommonAPIResForOrder();
        res.setSuccess(false);
        res.setResponseMessage(message);
        res.setOrderID(orderID);
        return res;
    }

}

