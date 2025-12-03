package com.platera.mapper;

import com.platera.dto.MenuCategoryRequest;
import com.platera.dto.MenuCategoryResponse;
import com.platera.model.MenuCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuCategoryMapper {
    MenuCategoryResponse toDto(MenuCategory menuCategory);
    MenuCategory toEntity(MenuCategoryRequest request);
}
