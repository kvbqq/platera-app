package com.platera.dto;

import com.platera.model.Role;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String email;
    private Role role;
}
