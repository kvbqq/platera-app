package com.platera.controller;

import com.platera.dto.ReservationRequest;
import com.platera.model.Reservation;
import com.platera.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createReservation(@RequestBody ReservationRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        reservationService.createReservation(request, userDetails.getUsername());
    }

    @GetMapping("/my")
    public List<Reservation> getMyReservations(@AuthenticationPrincipal UserDetails userDetails) {
        return reservationService.getMyReservations(userDetails.getUsername());
    }

    @GetMapping("/restaurant/{restaurantId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public List<Reservation> getRestaurantReservations(@PathVariable Long restaurantId) {
        return reservationService.getRestaurantReservations(restaurantId);
    }
}