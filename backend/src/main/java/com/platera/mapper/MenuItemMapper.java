package com.platera.mapper;

import com.platera.dto.MenuItemRequest;
import com.platera.dto.MenuItemResponse;
import com.platera.model.MenuItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {
    MenuItemResponse toDto(MenuItem menuItem);

    MenuItem toEntity(MenuItemRequest request);
}
