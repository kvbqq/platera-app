package com.platera.config;

import com.platera.model.*;
import com.platera.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // 1. Dodaj użytkowników (jeśli nie istnieją)
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .email("admin@platera.pl")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.ADMIN)
                    .build();

            User manager = User.builder()
                    .email("manager@platera.pl")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.MANAGER)
                    .build();

            User user = User.builder()
                    .email("user@platera.pl")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.USER)
                    .build();

            userRepository.saveAll(Arrays.asList(admin, manager, user));
            System.out.println("✅ Dodano użytkowników testowych (hasło: 'password')");
        }

        // 2. Dodaj Restauracje
        if (restaurantRepository.count() == 0) {
            // Restauracja 1: Włoska
            Restaurant italian = new Restaurant();
            italian.setName("Pizzeria Napoli");
            italian.setCity("Warszawa");
            italian.setAddress("ul. Marszałkowska 1");
            italian.setDescription("Prawdziwa włoska pizza na cienkim cieście. Najlepsze składniki prosto z Włoch.");
            restaurantRepository.save(italian);

            // Restauracja 2: Burgery
            Restaurant burger = new Restaurant();
            burger.setName("Burgerownia u Jana");
            burger.setCity("Kraków");
            burger.setAddress("Rynek Główny 12");
            burger.setDescription("Soczyste burgery w 100% z wołowiny. Robimy to z pasją.");
            restaurantRepository.save(burger);

            // 3. Dodaj Menu do Pizzerii (Restauracja 1)
            MenuCategory pizzaCategory = MenuCategory.builder()
                    .name("Pizze")
                    .restaurant(italian)
                    .build();

            MenuCategory drinksCategory = MenuCategory.builder()
                    .name("Napoje")
                    .restaurant(italian)
                    .build();

            menuCategoryRepository.saveAll(Arrays.asList(pizzaCategory, drinksCategory));

            MenuItem margherita = MenuItem.builder()
                    .name("Margherita")
                    .description("Sos pomidorowy, mozzarella, bazylia")
                    .price(new BigDecimal("32.00"))
                    .category(pizzaCategory)
                    .build();

            MenuItem pepperoni = MenuItem.builder()
                    .name("Pepperoni")
                    .description("Sos pomidorowy, mozzarella, salami piccante")
                    .price(new BigDecimal("38.00"))
                    .category(pizzaCategory)
                    .build();

            MenuItem cola = MenuItem.builder()
                    .name("Coca-Cola 0.5l")
                    .description("Zimny napój gazowany")
                    .price(new BigDecimal("9.00"))
                    .category(drinksCategory)
                    .build();

            menuItemRepository.saveAll(Arrays.asList(margherita, pepperoni, cola));

            System.out.println("✅ Dodano restauracje i menu testowe");
        }
    }
}