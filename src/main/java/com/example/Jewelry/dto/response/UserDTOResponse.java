package com.example.Jewelry.dto.response;

import com.example.Jewelry.dto.UserDTO;
import lombok.Data;

@Data
public class UserDTOResponse extends CommonApiResponse {

    private UserDTO data;

}
