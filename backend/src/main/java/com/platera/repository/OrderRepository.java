package com.platera.repository;

import com.platera.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByRestaurantId(Long restaurantId);
    List<Order> findByUserId(Long userId);

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.restaurant.id = :restaurantId AND o.status = 'PAID'")
    BigDecimal countTotalRevenue(@Param("restaurantId") Long restaurantId);

    @Query("SELECT oi.name, SUM(oi.quantity) as totalQty " +
            "FROM OrderItem oi " +
            "JOIN oi.order o " +
            "WHERE o.restaurant.id = :restaurantId " +
            "GROUP BY oi.name " +
            "ORDER BY totalQty DESC")
    List<Object[]> findBestSellingItems(@Param("restaurantId") Long restaurantId);
}