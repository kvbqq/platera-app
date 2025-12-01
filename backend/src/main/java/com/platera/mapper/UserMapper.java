package com.platera.mapper;

import com.platera.dto.UserDto;
import com.platera.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
