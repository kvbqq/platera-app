package com.platera.service;

import com.platera.dto.*;
import com.platera.mapper.*;
import com.platera.model.*;
import com.platera.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final RestaurantRepository restaurantRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final MenuCategoryMapper menuCategoryMapper;
    private final MenuItemMapper menuItemMapper;

    public List<MenuCategoryResponse> getMenu(Long restaurantId) {
        return menuCategoryRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(menuCategoryMapper::toDto)
                .toList();
    }

    @Transactional
    public MenuCategoryResponse createMenuCategory(Long restaurantId, MenuCategoryRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        MenuCategory category = MenuCategory.builder()
                .name(request.getName())
                .restaurant(restaurant)
                .build();

        return menuCategoryMapper.toDto(menuCategoryRepository.save(category));
    }

    @Transactional
    public void deleteMenuCategory(Long restaurantId, Long categoryId) {
        MenuCategory category = menuCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        menuCategoryRepository.delete(category);
    }

    @Transactional
    public MenuItemResponse createMenuItem(Long categoryId, MenuItemRequest request) {

        MenuCategory category = menuCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        MenuItem item = menuItemMapper.toEntity(request);
        item.setCategory(category);

        return menuItemMapper.toDto(menuItemRepository.save(item));
    }

    @Transactional
    public void deleteMenuItem(Long itemId) {
        if (!menuItemRepository.existsById(itemId)) {
            throw new RuntimeException("Item not found");
        }
        menuItemRepository.deleteById(itemId);
    }
}