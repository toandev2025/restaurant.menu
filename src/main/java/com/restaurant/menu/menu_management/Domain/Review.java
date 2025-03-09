package com.restaurant.menu.menu_management.Domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Người viết đánh giá

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order; // Mỗi đơn hàng chỉ có 1 review

    @Column(nullable = false)
    private int rating; // 1 - 5 sao

    private String comment;

    @Column(nullable = false)
    private LocalDateTime reviewTime = LocalDateTime.now();
}
