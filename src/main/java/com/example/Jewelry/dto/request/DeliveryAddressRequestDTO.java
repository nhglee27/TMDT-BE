package com.example.Jewelry.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
/***
 * DTO cho sổ địa chỉ, dùng cho request thêm và sửa
 */
public class DeliveryAddressRequestDTO {

    private String receiverFullName;
    private String buildingName;
    private String wardName;
    private String districtName;
    private String provinceName;
    private String contactNumber;
    private boolean isWorkAddress;

}
