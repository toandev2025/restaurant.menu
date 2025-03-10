package com.restaurant.menu.menu_management.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "dishes", indexes = {
        @Index(name = "idx_category_id", columnList = "category_id")
})
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Double price;

    private String imageUrl;

    @Column(nullable = false)
    private Boolean available = true; // Món ăn có sẵn hay không

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category; // Liên kết với danh mục món ăn
}