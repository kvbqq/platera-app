package com.platera.service;

import com.platera.dto.MenuCategoryRequest;
import com.platera.dto.MenuCategoryResponse;
import com.platera.dto.MenuItemRequest;
import com.platera.dto.MenuItemResponse;
import com.platera.mapper.MenuCategoryMapper;
import com.platera.mapper.MenuItemMapper;
import com.platera.model.MenuCategory;
import com.platera.model.MenuItem;
import com.platera.model.Restaurant;
import com.platera.repository.MenuCategoryRepository;
import com.platera.repository.MenuItemRepository;
import com.platera.repository.RestaurantRepository;
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

        if (!category.getRestaurant().getId().equals(restaurantId)) {
            throw new RuntimeException("Category not found in this restaurant");
        }

        menuCategoryRepository.delete(category);
    }

    @Transactional
    public MenuItemResponse createMenuItem(MenuItemRequest request) {
        MenuCategory category = menuCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return menuItemMapper.toDto(menuItemRepository.save(menuItemMapper.toEntity(request)));
    }

    @Transactional
    public void deleteMenuItem(Long itemId) {
        MenuItem item = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        menuItemRepository.delete(item);
    }
}
