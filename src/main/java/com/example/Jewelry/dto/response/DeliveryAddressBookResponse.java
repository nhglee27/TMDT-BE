package com.example.Jewelry.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.example.Jewelry.dto.DeliveryAddressDTO;

import lombok.Data;

@Data
public class DeliveryAddressBookResponse extends CommonApiResponse {

    private List<DeliveryAddressDTO> addresses = new ArrayList<>();
    private DeliveryAddressDTO address;

}
