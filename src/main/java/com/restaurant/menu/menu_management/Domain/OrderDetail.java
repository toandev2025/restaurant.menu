package com.restaurant.menu.menu_management.Domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order; // Đơn hàng liên kết

    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish; // Món ăn liên kết

    @Column(nullable = false)
    private Integer quantity; // Số lượng món ăn

    @Column(nullable = false)
    private Double unitPrice; // Giá của một đơn vị món ăn tại thời điểm đặt hàng

    @Column(nullable = false)
    private Double subtotal; // Tổng giá trị cho món ăn này (quantity * unitPrice)

    private String note; // Ghi chú cụ thể cho món ăn (nếu có)

    @PrePersist
    @PreUpdate
    public void calculateSubtotal() {
        this.subtotal = this.unitPrice * this.quantity;
    }
}