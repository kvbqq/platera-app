package com.platera.service;

import com.platera.dto.OrderRequest;
import com.platera.dto.RestaurantStatsResponse;
import com.platera.model.*;
import com.platera.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createOrder(OrderRequest request, String userEmail) {
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        User user = null;
        if (userEmail != null) {
            user = userRepository.findByEmail(userEmail).orElse(null);
        }

        Order order = Order.builder()
                .restaurant(restaurant)
                .user(user)
                .status(OrderStatus.NEW)
                .items(new ArrayList<>())
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (OrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemRequest.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .menuItem(menuItem)
                    .name(menuItem.getName())
                    .price(menuItem.getPrice())
                    .quantity(itemRequest.getQuantity())
                    .build();

            order.getItems().add(orderItem);
            total = total.add(menuItem.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
        }

        order.setTotalPrice(total);
        orderRepository.save(order);
    }

    public List<Order> getMyOrders(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUserId(user.getId());
    }

    public List<Order> getRestaurantOrders(Long restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId);
    }

    @Transactional
    public void updateStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
    }

    public RestaurantStatsResponse getStats(Long restaurantId) {
        BigDecimal revenue = orderRepository.countTotalRevenue(restaurantId);
        if (revenue == null) revenue = BigDecimal.ZERO;

        List<Object[]> bestSellersRaw = orderRepository.findBestSellingItems(restaurantId);

        List<RestaurantStatsResponse.BestSellingItem> bestSellers = bestSellersRaw.stream()
                .limit(5)
                .map(obj -> new RestaurantStatsResponse.BestSellingItem((String) obj[0], (Long) obj[1]))
                .collect(Collectors.toList());

        return new RestaurantStatsResponse(revenue, bestSellers);
    }
}