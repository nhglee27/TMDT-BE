package com.example.Jewelry.dto;

import java.beans.BeanProperty;

import org.springframework.beans.BeanUtils;

import com.example.Jewelry.entity.DeliveryAddress;

import lombok.Data;

@Data
public class DeliveryAddressDTO {
    
    private int id;
    /** Mã chủ nhân của địa chỉ giao hàng, dùng để biết địa chỉ thuộc về sổ địa chỉ của người dùng nào */
    private int userID;
    /** Tên người nhận (không nhất thiết là chính người dùng đó) */
    private String receiverName;
    /** Số nhà */
    private String buildingAddress;
    /** Phường / Xã */
    private String wardName;
    /** Quận / Huyện */
    private String districtName;
    /** Tỉnh / Thành Phố */
    private String provinceName;
    /** Điện thoại liên lạc */
    private String contactNumber;
    /** Địa chỉ giao hàng này có là cá nhân hay nơi làm */
    private boolean isWorkAddress;

    public static DeliveryAddressDTO convertDeliveryAddress(DeliveryAddress address) {
        DeliveryAddressDTO result = new DeliveryAddressDTO();
        BeanUtils.copyProperties(address, result);
        result.setUserID(address.getUser().getId());
        return result;
    }
}
