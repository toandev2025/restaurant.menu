package com.restaurant.menu.menu_management.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Người đặt món

    @Column(nullable = false)
    private String orderType; // "DINE_IN" hoặc "TAKEAWAY"

    private Integer tableNumber; // Nếu là DINE_IN, bắt buộc nhập

    private String location; // Nếu là TAKEAWAY, bắt buộc nhập

    @Column(nullable = false)
    private Double totalPrice = 0.0; // Tính tự động từ OrderDetail

    @Column(nullable = false)
    private String status = "PENDING"; // ✅ Đặt mặc định là PENDING
    // PENDING, CONFIRMED, READY, CANCELLED, COMPLETED

    private LocalDateTime orderTime = LocalDateTime.now();

    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;
}
