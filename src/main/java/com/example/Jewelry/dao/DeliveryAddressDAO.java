package com.example.Jewelry.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Jewelry.entity.DeliveryAddress;
import com.example.Jewelry.entity.User;

public interface DeliveryAddressDAO extends JpaRepository<DeliveryAddress, Integer> {

    List<DeliveryAddress> findAllByUser(User user);

}
