package com.example.ruijiwaimai.dto;

import com.example.ruijiwaimai.entity.User;
import lombok.Data;

@Data
public class UserDto extends User {
    private String code;
}
