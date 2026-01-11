package com.platera.controller;

import com.platera.dto.OrderRequest;
import com.platera.dto.RestaurantStatsResponse;
import com.platera.model.Order;
import com.platera.model.OrderStatus;
import com.platera.repository.OrderRepository;
import com.platera.service.OrderService;
import com.platera.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private final PdfService pdfService;
    private final OrderRepository orderRepository;

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
    public void updateStatus(@PathVariable Long id, @RequestBody String status) {
        String cleanStatus = status.replace("\"", "");
        orderService.updateStatus(id, OrderStatus.valueOf(cleanStatus));
    }

    @GetMapping("/restaurant/{restaurantId}/stats")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public RestaurantStatsResponse getStats(@PathVariable Long restaurantId) {
        return orderService.getStats(restaurantId);
    }

    @GetMapping(value = "/{id}/receipt", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getReceipt(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return pdfService.generateReceipt(order);
    }
}