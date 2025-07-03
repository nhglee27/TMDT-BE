package com.example.Jewelry.controller;

import com.example.Jewelry.resource.UserResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import com.example.Jewelry.dto.DeliveryAddressDTO;
import com.example.Jewelry.dto.response.DeliveryAddressBookResponse;
import com.example.Jewelry.entity.DeliveryAddress;
import com.example.Jewelry.entity.User;
import com.example.Jewelry.service.DeliveryAddressService;
import com.example.Jewelry.service.UserService;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/api/delivery")
@CrossOrigin(origins = "http://localhost:3000")
public class DeliveryAddressController {
    private final Logger LOG = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    private UserService userService;

    /** Sổ địa chỉ */
    @Autowired
    private DeliveryAddressService deliveryAddressService;
    
    /** lấy sổ địa chỉ */
    @GetMapping("/fetch-user/{userID}")
    public ResponseEntity<DeliveryAddressBookResponse> getDeliveryAddresses(@PathVariable int userID) {
        DeliveryAddressBookResponse response = new DeliveryAddressBookResponse();
        User user = userService.getUserById(userID);
        if (user == null) {
            response.setSuccess(false);
            response.setResponseMessage("User is not found.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        List<DeliveryAddress> addresses = deliveryAddressService.getByUserID(userID);
        if (addresses == null) {
            response.setSuccess(false);
            response.setResponseMessage("User not have any delivery address.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        List<DeliveryAddressDTO> result = addresses.stream().map((addr) -> DeliveryAddressDTO.convertDeliveryAddress(addr)).toList();
        response.setSuccess(true);
        response.setResponseMessage("Lấy sổ địa chỉ thành công!");
        response.setAddresses(result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private DeliveryAddress toDeliveryAddress(DeliveryAddressDTO addressDTO) {
        DeliveryAddress address = new DeliveryAddress();
        BeanUtils.copyProperties(addressDTO, address, "userID");

        User addressOwner = userService.getUserById(addressDTO.getUserID());
        address.setUser(addressOwner);

        return address;
    }

    /** Thêm địa chỉ mới cho người dùng */
    @PostMapping("/add")
    public ResponseEntity<DeliveryAddressBookResponse> addNewDeliveryAddress(@RequestBody DeliveryAddressDTO newAddressDTO) {
        LOG.info("Add a Address Request DTO " + newAddressDTO.toString());
        DeliveryAddressBookResponse response = new DeliveryAddressBookResponse();
        DeliveryAddress addingAddress = toDeliveryAddress(newAddressDTO);
        LOG.info("Add a Address Request " + addingAddress.toString());
        DeliveryAddress ehrm = deliveryAddressService.addAddress(addingAddress);
        response.setAddress(DeliveryAddressDTO.convertDeliveryAddress(ehrm));
        response.setSuccess(true);
        response.setResponseMessage("Thêm thành công địa chỉ");
        return new ResponseEntity<DeliveryAddressBookResponse>(response, HttpStatus.OK);
    }

    /** Thêm địa chỉ mới cho người dùng */
    @PostMapping("/update")
    public ResponseEntity<DeliveryAddressBookResponse> updateNewDeliveryAddress(@RequestBody DeliveryAddressDTO newAddressDTO) {
        DeliveryAddressBookResponse response = new DeliveryAddressBookResponse();
        DeliveryAddress addingAddress = toDeliveryAddress(newAddressDTO);

        DeliveryAddress ehrm = deliveryAddressService.updateDeliveryAddress(addingAddress);
        response.setAddress(DeliveryAddressDTO.convertDeliveryAddress(ehrm));
        response.setSuccess(true);
        response.setResponseMessage("Cập nhật thành công địa chỉ");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    

}
