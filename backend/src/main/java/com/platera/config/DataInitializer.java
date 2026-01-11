package com.platera.config;

import com.platera.model.*;
import com.platera.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestaurantTableRepository restaurantTableRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public void run(String... args) {
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
        }

        if (restaurantRepository.count() == 0) {
            Restaurant italian = new Restaurant();
            italian.setName("Pizzeria Napoli");
            italian.setCity("Warszawa");
            italian.setAddress("ul. Marszałkowska 1");
            italian.setDescription("Prawdziwa włoska pizza na cienkim cieście. Najlepsze składniki prosto z Włoch.");
            restaurantRepository.save(italian);

            MenuCategory pizzeItalian = MenuCategory.builder()
                    .name("Pizze")
                    .restaurant(italian)
                    .build();
            MenuCategory italianDrinks = MenuCategory.builder()
                    .name("Napoje")
                    .restaurant(italian)
                    .build();
            menuCategoryRepository.saveAll(Arrays.asList(pizzeItalian, italianDrinks));

            MenuItem margherita = MenuItem.builder()
                    .name("Margherita")
                    .description("Sos pomidorowy, mozzarella, bazylia")
                    .price(new BigDecimal("32.00"))
                    .category(pizzeItalian)
                    .build();
            MenuItem pepperoni = MenuItem.builder()
                    .name("Pepperoni")
                    .description("Sos pomidorowy, mozzarella, salami piccante")
                    .price(new BigDecimal("38.00"))
                    .category(pizzeItalian)
                    .build();
            MenuItem capricciosa = MenuItem.builder()
                    .name("Capricciosa")
                    .description("Sos pomidorowy, mozzarella, szynka, pieczarki, karczochy, oliwki")
                    .price(new BigDecimal("40.00"))
                    .category(pizzeItalian)
                    .build();
            MenuItem prosciutto = MenuItem.builder()
                    .name("Prosciutto e funghi")
                    .description("Sos pomidorowy, mozzarella, szynka parmeńska, pieczarki")
                    .price(new BigDecimal("42.00"))
                    .category(pizzeItalian)
                    .build();
            MenuItem cola = MenuItem.builder()
                    .name("Coca‑Cola 0.5l")
                    .description("Zimny napój gazowany")
                    .price(new BigDecimal("9.00"))
                    .category(italianDrinks)
                    .build();
            MenuItem fanta = MenuItem.builder()
                    .name("Fanta 0.5l")
                    .description("Napój pomarańczowy gazowany")
                    .price(new BigDecimal("9.00"))
                    .category(italianDrinks)
                    .build();
            menuItemRepository.saveAll(Arrays.asList(margherita, pepperoni, capricciosa, prosciutto, cola, fanta));

            RestaurantTable itTable1 = new RestaurantTable(null, "Stolik 1", 2, italian);
            RestaurantTable itTable2 = new RestaurantTable(null, "Stolik 2", 2, italian);
            RestaurantTable itTable3 = new RestaurantTable(null, "Stolik 3", 4, italian);
            RestaurantTable itTable4 = new RestaurantTable(null, "Stolik 4", 4, italian);
            RestaurantTable itTable5 = new RestaurantTable(null, "Stolik 5", 6, italian);
            restaurantTableRepository.saveAll(Arrays.asList(itTable1, itTable2, itTable3, itTable4, itTable5));

            LocalDateTime now = LocalDateTime.now();
            Reservation itRes1 = Reservation.builder()
                    .startDateTime(now.plusDays(1).withHour(18).withMinute(0).withSecond(0).withNano(0))
                    .endDateTime(now.plusDays(1).withHour(20).withMinute(0).withSecond(0).withNano(0))
                    .customerName("Anna Nowak")
                    .customerPhone("123456789")
                    .comments("Rezerwacja urodzinowa")
                    .status(ReservationStatus.CONFIRMED)
                    .table(itTable1)
                    .restaurant(italian)
                    .build();
            Reservation itRes2 = Reservation.builder()
                    .startDateTime(now.plusDays(1).withHour(20).withMinute(30).withSecond(0).withNano(0))
                    .endDateTime(now.plusDays(1).withHour(22).withMinute(30).withSecond(0).withNano(0))
                    .customerName("Tomasz Wiśniewski")
                    .customerPhone("987654321")
                    .comments("Kolacja we dwoje")
                    .status(ReservationStatus.PENDING)
                    .table(itTable2)
                    .restaurant(italian)
                    .build();
            reservationRepository.saveAll(Arrays.asList(itRes1, itRes2));

            Restaurant burger = new Restaurant();
            burger.setName("Burgerownia u Jana");
            burger.setCity("Kraków");
            burger.setAddress("Rynek Główny 12");
            burger.setDescription("Soczyste burgery w 100% z wołowiny. Robimy to z pasją.");
            restaurantRepository.save(burger);

            MenuCategory burgersCat = MenuCategory.builder()
                    .name("Burgery")
                    .restaurant(burger)
                    .build();
            MenuCategory burgerExtras = MenuCategory.builder()
                    .name("Dodatki")
                    .restaurant(burger)
                    .build();
            MenuCategory burgerDrinks = MenuCategory.builder()
                    .name("Napoje")
                    .restaurant(burger)
                    .build();
            menuCategoryRepository.saveAll(Arrays.asList(burgersCat, burgerExtras, burgerDrinks));

            // Menu items for Burgerownia
            MenuItem classicBurger = MenuItem.builder()
                    .name("Burger klasyczny")
                    .description("Wołowina, ser, sos burgerowy, sałata, pomidor")
                    .price(new BigDecimal("25.00"))
                    .category(burgersCat)
                    .build();
            MenuItem cheeseBurger = MenuItem.builder()
                    .name("Cheeseburger")
                    .description("Wołowina, podwójny ser, ketchup")
                    .price(new BigDecimal("28.00"))
                    .category(burgersCat)
                    .build();
            MenuItem vegeBurger = MenuItem.builder()
                    .name("Burger wege")
                    .description("Kotlet roślinny, warzywa, sos wegański")
                    .price(new BigDecimal("26.00"))
                    .category(burgersCat)
                    .build();
            MenuItem fries = MenuItem.builder()
                    .name("Frytki")
                    .description("Porcja frytek belgijskich")
                    .price(new BigDecimal("8.00"))
                    .category(burgerExtras)
                    .build();
            MenuItem onionRings = MenuItem.builder()
                    .name("Krążki cebulowe")
                    .description("Chrupiące krążki cebulowe")
                    .price(new BigDecimal("9.00"))
                    .category(burgerExtras)
                    .build();
            MenuItem pepsi = MenuItem.builder()
                    .name("Pepsi 0.5l")
                    .description("Zimny napój gazowany")
                    .price(new BigDecimal("6.00"))
                    .category(burgerDrinks)
                    .build();
            MenuItem sprite = MenuItem.builder()
                    .name("Sprite 0.5l")
                    .description("Napój cytrynowy gazowany")
                    .price(new BigDecimal("6.00"))
                    .category(burgerDrinks)
                    .build();
            menuItemRepository.saveAll(Arrays.asList(classicBurger, cheeseBurger, vegeBurger, fries, onionRings, pepsi, sprite));

            RestaurantTable burTable1 = new RestaurantTable(null, "Stolik 1", 2, burger);
            RestaurantTable burTable2 = new RestaurantTable(null, "Stolik 2", 4, burger);
            RestaurantTable burTable3 = new RestaurantTable(null, "Stolik 3", 4, burger);
            RestaurantTable burTable4 = new RestaurantTable(null, "Stolik 4", 6, burger);
            restaurantTableRepository.saveAll(Arrays.asList(burTable1, burTable2, burTable3, burTable4));

            Reservation burRes1 = Reservation.builder()
                    .startDateTime(now.plusDays(2).withHour(19).withMinute(0).withSecond(0).withNano(0))
                    .endDateTime(now.plusDays(2).withHour(21).withMinute(0).withSecond(0).withNano(0))
                    .customerName("Michał Zieliński")
                    .customerPhone("500600700")
                    .comments("Spotkanie z przyjaciółmi")
                    .status(ReservationStatus.CONFIRMED)
                    .table(burTable3)
                    .restaurant(burger)
                    .build();
            Reservation burRes2 = Reservation.builder()
                    .startDateTime(now.plusDays(2).withHour(21).withMinute(30).withSecond(0).withNano(0))
                    .endDateTime(now.plusDays(2).withHour(23).withMinute(0).withSecond(0).withNano(0))
                    .customerName("Karolina Wiśniewska")
                    .customerPhone("600700800")
                    .comments("Impreza urodzinowa")
                    .status(ReservationStatus.PENDING)
                    .table(burTable1)
                    .restaurant(burger)
                    .build();
            reservationRepository.saveAll(Arrays.asList(burRes1, burRes2));

            Restaurant sushi = new Restaurant();
            sushi.setName("Sushi House");
            sushi.setCity("Gdańsk");
            sushi.setAddress("ul. Długa 5");
            sushi.setDescription("Autentyczne japońskie sushi i przystawki.");
            restaurantRepository.save(sushi);

            MenuCategory sushiCat = MenuCategory.builder()
                    .name("Sushi")
                    .restaurant(sushi)
                    .build();
            MenuCategory sushiStarters = MenuCategory.builder()
                    .name("Przystawki")
                    .restaurant(sushi)
                    .build();
            MenuCategory sushiDrinks = MenuCategory.builder()
                    .name("Napoje")
                    .restaurant(sushi)
                    .build();
            menuCategoryRepository.saveAll(Arrays.asList(sushiCat, sushiStarters, sushiDrinks));

            MenuItem nigiri = MenuItem.builder()
                    .name("Nigiri łosoś")
                    .description("Ryż, łosoś")
                    .price(new BigDecimal("20.00"))
                    .category(sushiCat)
                    .build();
            MenuItem california = MenuItem.builder()
                    .name("California roll")
                    .description("Ryż, ogórek, awokado, paluszek krabowy, sezam")
                    .price(new BigDecimal("22.00"))
                    .category(sushiCat)
                    .build();
            MenuItem tunaMaki = MenuItem.builder()
                    .name("Maki tuńczyk")
                    .description("Ryż, tuńczyk, nori")
                    .price(new BigDecimal("18.00"))
                    .category(sushiCat)
                    .build();
            MenuItem miso = MenuItem.builder()
                    .name("Zupa miso")
                    .description("Tradycyjna japońska zupa miso")
                    .price(new BigDecimal("10.00"))
                    .category(sushiStarters)
                    .build();
            MenuItem edamame = MenuItem.builder()
                    .name("Edamame")
                    .description("Gotowane, solone strączki soi")
                    .price(new BigDecimal("12.00"))
                    .category(sushiStarters)
                    .build();
            MenuItem greenTea = MenuItem.builder()
                    .name("Zielona herbata")
                    .description("Gorąca japońska herbata")
                    .price(new BigDecimal("6.00"))
                    .category(sushiDrinks)
                    .build();
            MenuItem water = MenuItem.builder()
                    .name("Woda mineralna")
                    .description("Woda niegazowana 0.5l")
                    .price(new BigDecimal("4.00"))
                    .category(sushiDrinks)
                    .build();
            menuItemRepository.saveAll(Arrays.asList(nigiri, california, tunaMaki, miso, edamame, greenTea, water));

            RestaurantTable sushiTable1 = new RestaurantTable(null, "Stolik 1", 2, sushi);
            RestaurantTable sushiTable2 = new RestaurantTable(null, "Stolik 2", 3, sushi);
            RestaurantTable sushiTable3 = new RestaurantTable(null, "Stolik 3", 6, sushi);
            restaurantTableRepository.saveAll(Arrays.asList(sushiTable1, sushiTable2, sushiTable3));

            Reservation sushiRes1 = Reservation.builder()
                    .startDateTime(now.plusDays(1).withHour(17).withMinute(0).withSecond(0).withNano(0))
                    .endDateTime(now.plusDays(1).withHour(19).withMinute(0).withSecond(0).withNano(0))
                    .customerName("Ewa Nowak")
                    .customerPhone("700800900")
                    .comments("Kolacja sushi")
                    .status(ReservationStatus.CONFIRMED)
                    .table(sushiTable1)
                    .restaurant(sushi)
                    .build();
            reservationRepository.save(sushiRes1);

            Restaurant vegan = new Restaurant();
            vegan.setName("Vegan Bistro");
            vegan.setCity("Poznań");
            vegan.setAddress("ul. Święty Marcin 34");
            vegan.setDescription("W 100% roślinne burgery, sałatki i desery.");
            restaurantRepository.save(vegan);

            MenuCategory veganBurgers = MenuCategory.builder()
                    .name("Burgery")
                    .restaurant(vegan)
                    .build();
            MenuCategory veganSalads = MenuCategory.builder()
                    .name("Sałatki")
                    .restaurant(vegan)
                    .build();
            MenuCategory veganDesserts = MenuCategory.builder()
                    .name("Desery")
                    .restaurant(vegan)
                    .build();
            MenuCategory veganDrinks = MenuCategory.builder()
                    .name("Napoje")
                    .restaurant(vegan)
                    .build();
            menuCategoryRepository.saveAll(Arrays.asList(veganBurgers, veganSalads, veganDesserts, veganDrinks));

            MenuItem chickpeaBurger = MenuItem.builder()
                    .name("Burger z ciecierzycy")
                    .description("Kotlet z ciecierzycy, warzywa, sos czosnkowy")
                    .price(new BigDecimal("24.00"))
                    .category(veganBurgers)
                    .build();
            MenuItem beanBurger = MenuItem.builder()
                    .name("Burger z fasoli")
                    .description("Kotlet z czerwonej fasoli, warzywa, sos BBQ")
                    .price(new BigDecimal("23.00"))
                    .category(veganBurgers)
                    .build();
            MenuItem quinoaSalad = MenuItem.builder()
                    .name("Sałatka z quinoa")
                    .description("Quinoa, warzywa, sos winegret")
                    .price(new BigDecimal("18.00"))
                    .category(veganSalads)
                    .build();
            MenuItem greekSalad = MenuItem.builder()
                    .name("Sałatka grecka (wegańska)")
                    .description("Sałata, oliwki, pomidory, tofu feta")
                    .price(new BigDecimal("17.00"))
                    .category(veganSalads)
                    .build();
            MenuItem brownie = MenuItem.builder()
                    .name("Brownie wegańskie")
                    .description("Brownie z gorzkiej czekolady i orzechów")
                    .price(new BigDecimal("14.00"))
                    .category(veganDesserts)
                    .build();
            MenuItem fruitTart = MenuItem.builder()
                    .name("Tarta owocowa")
                    .description("Tarta z sezonowymi owocami")
                    .price(new BigDecimal("13.00"))
                    .category(veganDesserts)
                    .build();
            MenuItem smoothie = MenuItem.builder()
                    .name("Smoothie truskawkowe")
                    .description("Truskawki, banan, mleko roślinne")
                    .price(new BigDecimal("10.00"))
                    .category(veganDrinks)
                    .build();
            MenuItem lemonade = MenuItem.builder()
                    .name("Lemoniada")
                    .description("Lemoniada z cytryn i mięty")
                    .price(new BigDecimal("8.00"))
                    .category(veganDrinks)
                    .build();
            menuItemRepository.saveAll(Arrays.asList(chickpeaBurger, beanBurger, quinoaSalad, greekSalad, brownie, fruitTart, smoothie, lemonade));

            RestaurantTable vegTable1 = new RestaurantTable(null, "Stolik 1", 2, vegan);
            RestaurantTable vegTable2 = new RestaurantTable(null, "Stolik 2", 2, vegan);
            RestaurantTable vegTable3 = new RestaurantTable(null, "Stolik 3", 4, vegan);
            RestaurantTable vegTable4 = new RestaurantTable(null, "Stolik 4", 6, vegan);
            restaurantTableRepository.saveAll(Arrays.asList(vegTable1, vegTable2, vegTable3, vegTable4));

            Reservation vegRes1 = Reservation.builder()
                    .startDateTime(now.plusDays(1).withHour(12).withMinute(0).withSecond(0).withNano(0))
                    .endDateTime(now.plusDays(1).withHour(13).withMinute(30).withSecond(0).withNano(0))
                    .customerName("Piotr Kaczmarek")
                    .customerPhone("800900100")
                    .comments("Lunch biznesowy")
                    .status(ReservationStatus.CONFIRMED)
                    .table(vegTable2)
                    .restaurant(vegan)
                    .build();
            reservationRepository.save(vegRes1);
        }
    }
}
