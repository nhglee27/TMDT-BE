package com.example.Jewelry.service.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Jewelry.dao.DeliveryAddressDAO;
import com.example.Jewelry.entity.DeliveryAddress;
import com.example.Jewelry.entity.User;
import com.example.Jewelry.service.DeliveryAddressService;
import com.example.Jewelry.service.UserService;
import com.fasterxml.jackson.databind.util.BeanUtil;

/***
 * Lớp hiện thực của service cho sổ địa chỉ
 */
@Service
public class DevileryAddressServiceImpl implements DeliveryAddressService {

    @Autowired
    private UserService userService;
    @Autowired
    private DeliveryAddressDAO deliveryAddressDAO;

    @Override
    public List<DeliveryAddress> getByUserID(int userID) {
        User user = userService.getUserById(userID);
        if (user == null) {
            return null;
        }
        return deliveryAddressDAO.findAllByUser(user);
    }

    @Override
    public DeliveryAddress addAddress(DeliveryAddress address) {
        return deliveryAddressDAO.save(address);
    }
    @Override
    public DeliveryAddress updateDeliveryAddress(DeliveryAddress address) {
        // Find by IDs
        Optional<DeliveryAddress> search = deliveryAddressDAO.findById(address.getId());
        // Return null because no delivery address founded with id to update
        if (!search.isPresent()) return null;

        // Otherwise, clone it without id and owner
        DeliveryAddress originalAddress = search.get();
        BeanUtils.copyProperties(address, originalAddress, "id", "owner");

        return deliveryAddressDAO.save(originalAddress);
    }

    @Override
    public boolean removeDeliveryAddress(DeliveryAddress address) {
        deliveryAddressDAO.delete(address);
        return deliveryAddressDAO.findById(address.getId()).isEmpty();
    }



}
