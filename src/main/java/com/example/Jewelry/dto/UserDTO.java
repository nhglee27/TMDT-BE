package com.example.Jewelry.dto;

import com.example.Jewelry.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private int id;
    private String gender;
    private String username;
    private String firstName;
    private String lastName;
    private String emailId;
    private String phoneNo;
    private String role;
    private String status;
    private String avatar;

    public static UserDTO toUserDtoEntity(User user) {
        UserDTO userDto = new UserDTO();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }
}
