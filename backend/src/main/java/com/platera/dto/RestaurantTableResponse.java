package com.platera.dto;

import lombok.Data;

@Data
public class RestaurantTableResponse {
    private Long id;
    private String label;
    private Integer capacity;
}
