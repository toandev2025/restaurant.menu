package com.restaurant.menu.menu_management.Domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_details", indexes = {
        @Index(name = "idx_order_id", columnList = "order_id"),
        @Index(name = "idx_dish_id", columnList = "dish_id")
})
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

    @Column(nullable = false)
    private Double priceAtOrderTime; // Giá của món ăn tại thời điểm đặt hàng

    private String note; // Ghi chú cụ thể cho món ăn (nếu có)

    public void calculateSubtotal() {
        if (this.unitPrice == null || this.quantity == null) {
            throw new IllegalStateException("Unit price hoặc quantity không được để null");
        }
        this.subtotal = this.unitPrice * this.quantity;
    }

    // Thêm setter cho priceAtOrderTime để tránh lỗi
    public void setPriceAtOrderTime(Double priceAtOrderTime) {
        this.priceAtOrderTime = priceAtOrderTime;
    }
}
