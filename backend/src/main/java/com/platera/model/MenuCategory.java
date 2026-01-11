package com.platera.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "menu_categories")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @JsonIgnore
    @ToString.Exclude
    private Restaurant restaurant;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<MenuItem> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuCategory menuCategory = (MenuCategory) o;
        return id != null && id.equals(menuCategory.id);
  }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
