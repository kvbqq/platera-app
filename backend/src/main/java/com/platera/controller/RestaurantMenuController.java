package com.platera.controller;

import com.platera.dto.MenuCategoryRequest;
import com.platera.dto.MenuCategoryResponse;
import com.platera.dto.MenuItemRequest;
import com.platera.dto.MenuItemResponse;
import com.platera.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/menu")
@RequiredArgsConstructor
public class RestaurantMenuController {
    private final MenuService menuService;

    @GetMapping
    public List<MenuCategoryResponse> getMenu(@PathVariable Long restaurantId) {
        return menuService.getMenu(restaurantId);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public MenuCategoryResponse createMenuCategory(@PathVariable Long restaurantId, @RequestBody MenuCategoryRequest request) {
        return menuService.createMenuCategory(restaurantId, request);
    }

    @DeleteMapping("/categories/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMenuCategory(@PathVariable Long restaurantId, @PathVariable Long categoryId) {
        menuService.deleteMenuCategory(restaurantId, categoryId);
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemResponse createMenuItem(@RequestBody MenuItemRequest request) {
        return menuService.createMenuItem(request);
    }

    @DeleteMapping("/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMenuItem(@PathVariable Long itemId) {
        menuService.deleteMenuItem(itemId);
    }
}
