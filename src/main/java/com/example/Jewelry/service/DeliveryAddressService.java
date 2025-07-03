package com.example.Jewelry.service;

import java.util.List;

import com.example.Jewelry.entity.DeliveryAddress;

public interface DeliveryAddressService {

    List<DeliveryAddress> getByUserID(int userID);
    DeliveryAddress addAddress(DeliveryAddress address);
    DeliveryAddress updateDeliveryAddress(DeliveryAddress address);
    boolean removeDeliveryAddress(DeliveryAddress address);

}
