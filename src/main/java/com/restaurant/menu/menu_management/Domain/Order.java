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
@Table(name = "orders", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_status", columnList = "status")
})
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Người đặt hàng

    @Column(nullable = false)
    private String orderType; // "DINE_IN" hoặc "TAKEAWAY"

    private Integer tableNumber; // Bắt buộc nếu orderType là "DINE_IN"

    private String location; // Bắt buộc nếu orderType là "TAKEAWAY"

    @Column(nullable = false)
    private Double totalAmount = 0.0; // Tổng số tiền đơn hàng

    @Column(nullable = false)
    private String status; // Trạng thái đơn hàng: PENDING, CONFIRMED, READY, CANCELLED, COMPLETED

    @Column(nullable = false)
    private LocalDateTime orderTime = LocalDateTime.now(); // Thời gian đặt hàng

    private String paymentMethod; // Phương thức thanh toán: CASH, CREDIT_CARD, E_WALLET

    private String deliveryAddress; // Địa chỉ giao hàng (nếu có)

    private String phoneNumber; // Số điện thoại liên hệ

    private String note; // Ghi chú của khách hàng

    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails; // Danh sách chi tiết đơn hàng

    // Thêm setter cho totalAmount
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}