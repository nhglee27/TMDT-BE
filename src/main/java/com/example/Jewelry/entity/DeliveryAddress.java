package com.example.Jewelry.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class DeliveryAddress {

    /** Mã định danh */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /** Chủ nhân của địa chỉ giao hàng, dùng để biết địa chỉ thuộc về sổ địa chỉ của người dùng nào */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "owner_id", nullable = false)
    private User user;

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

}
