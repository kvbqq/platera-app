package com.platera.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantStatsResponse {
    private BigDecimal totalRevenue;
    private List<BestSellingItem> bestSellingItems;

    @Data
    @AllArgsConstructor
    public static class BestSellingItem {
        private String name;
        private Long quantity;
    }
}