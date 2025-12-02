package com.platera.service;

import com.platera.dto.RestaurantRequest;
import com.platera.dto.RestaurantResponse;
import com.platera.mapper.RestaurantMapper;
import com.platera.model.Restaurant;
import com.platera.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    public List<RestaurantResponse> getRestaurants(Pageable pageable) {
        return restaurantRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(restaurantMapper::toDto)
                .toList();
    }

    public RestaurantResponse getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        return restaurantMapper.toDto(restaurant);
    }

    public RestaurantResponse createRestaurant(RestaurantRequest request) {
        Restaurant restaurant = restaurantMapper.toEntity(request);

        return restaurantMapper.toDto(restaurantRepository.save(restaurant));
    }

    public RestaurantResponse updateRestaurant(Long id, RestaurantRequest request) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        restaurant.update(restaurantMapper.toEntity(request));

        return restaurantMapper.toDto(restaurantRepository.save(restaurant));
    }

    public void deleteRestaurant(Long id) {
        restaurantRepository.deleteById(id);
    }
}
