package com.platera.controller;

import com.platera.dto.RestaurantRequest;
import com.platera.dto.RestaurantResponse;
import com.platera.service.RestaurantService;
import com.platera.service.QrCodeService;
import com.platera.repository.RestaurantTableRepository;
import com.platera.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantTableRepository restaurantTableRepository;
    private final RestaurantRepository restaurantRepository;
    private final QrCodeService qrCodeService;

    @GetMapping
    public List<RestaurantResponse> getRestaurants(Pageable pageable) {
        return restaurantService.getRestaurants(pageable);
    }

    @GetMapping("/{id}")
    public RestaurantResponse getRestaurantById(@PathVariable Long id) {
        return restaurantService.getRestaurantById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public RestaurantResponse createRestaurant(@RequestBody RestaurantRequest request) {
        return restaurantService.createRestaurant(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public RestaurantResponse updateRestaurant(@PathVariable Long id, @RequestBody RestaurantRequest request) {
        return restaurantService.updateRestaurant(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public void deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
    }

    @GetMapping("/{id}/tables")
    public List<com.platera.dto.RestaurantTableResponse> getTables(@PathVariable Long id) {
        return restaurantTableRepository.findByRestaurantId(id).stream()
                .map(t -> {
                    com.platera.dto.RestaurantTableResponse r = new com.platera.dto.RestaurantTableResponse();
                    r.setId(t.getId());
                    r.setLabel(t.getLabel());
                    r.setCapacity(t.getCapacity());
                    return r;
                }).toList();
    }

    @PostMapping("/{id}/tables")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Transactional
    public void addTable(@PathVariable Long id, @RequestBody com.platera.dto.RestaurantTableRequest req) {
        com.platera.model.Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        com.platera.model.RestaurantTable t = new com.platera.model.RestaurantTable();
        t.setLabel(req.getLabel());
        t.setCapacity(req.getCapacity());
        t.setRestaurant(restaurant);

        restaurantTableRepository.save(t);
    }

    @DeleteMapping("/tables/{tableId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Transactional
    public void deleteTable(@PathVariable Long tableId) {
        restaurantTableRepository.deleteById(tableId);
    }

    @GetMapping(value = "/tables/{tableId}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getTableQrCode(@PathVariable Long tableId) {
        com.platera.model.RestaurantTable table = restaurantTableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Table not found"));
        String url = "http://localhost:5173/restaurant/" + table.getRestaurant().getId() + "?tableId=" + table.getId();
        return qrCodeService.generateQrCode(url, 300, 300);
    }
}