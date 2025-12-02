package com.platera.mapper;

import com.platera.dto.RestaurantRequest;
import com.platera.dto.RestaurantResponse;
import com.platera.model.Restaurant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    RestaurantResponse toDto(Restaurant restaurant);

    Restaurant toEntity(RestaurantRequest request);
}
