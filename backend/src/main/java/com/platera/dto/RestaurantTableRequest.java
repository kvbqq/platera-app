package com.platera.dto;

import lombok.Data;

@Data
public class RestaurantTableRequest {
    private String label;
    private Integer capacity;
}
