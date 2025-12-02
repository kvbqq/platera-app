package com.platera.dto;

import lombok.Data;

@Data
public class RestaurantRequest {
    private String name;
    private String city;
    private String address;
    private String description;
}
