package com.platera.service;

import com.platera.dto.ReservationRequest;
import com.platera.model.*;
import com.platera.repository.ReservationRepository;
import com.platera.repository.RestaurantRepository;
import com.platera.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createReservation(ReservationRequest request, String userEmail) {
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Reservation reservation = Reservation.builder()
                .restaurant(restaurant)
                .user(user)
                .startDateTime(request.getStartDateTime())
                .endDateTime(request.getStartDateTime().plusMinutes(request.getDurationMinutes()))
                .customerName(request.getCustomerName())
                .customerPhone(request.getCustomerPhone())
                .comments(request.getComments())
                .status(ReservationStatus.PENDING)
                .build();

        reservationRepository.save(reservation);
    }

    public List<Reservation> getMyReservations(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return reservationRepository.findByUserId(user.getId());
    }

    public List<Reservation> getRestaurantReservations(Long restaurantId) {
        return reservationRepository.findByRestaurantId(restaurantId);
    }
}