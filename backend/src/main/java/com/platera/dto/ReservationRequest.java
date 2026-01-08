package com.platera.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservationRequest {
    private LocalDateTime startDateTime;
    private Integer durationMinutes;
    private Long tableId;
    private Long restaurantId;
    private String customerName;
    private String customerPhone;
    private String comments;
}