package com.platera.controller;

import com.platera.dto.OrderRequest;
import com.platera.dto.RestaurantStatsResponse;
import com.platera.model.Order;
import com.platera.model.OrderStatus;
import com.platera.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(@RequestBody OrderRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails != null ? userDetails.getUsername() : null;
        orderService.createOrder(request, email);
    }

    @GetMapping("/my")
    public List<Order> getMyOrders(@AuthenticationPrincipal UserDetails userDetails) {
        return orderService.getMyOrders(userDetails.getUsername());
    }

    @GetMapping("/restaurant/{restaurantId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public List<Order> getRestaurantOrders(@PathVariable Long restaurantId) {
        return orderService.getRestaurantOrders(restaurantId);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public void updateStatus(@PathVariable Long id, @RequestBody OrderStatus status) {
        orderService.updateStatus(id, status);
    }

    @GetMapping("/restaurant/{restaurantId}/stats")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public RestaurantStatsResponse getStats(@PathVariable Long restaurantId) {
        return orderService.getStats(restaurantId);
    }
}