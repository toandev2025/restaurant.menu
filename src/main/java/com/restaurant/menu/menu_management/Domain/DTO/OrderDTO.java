package com.restaurant.menu.menu_management.Domain.DTO;

import java.time.LocalDateTime;

import com.restaurant.menu.menu_management.Domain.Order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO {
    private Long id;
    private UserDTO user;
    private String orderType;
    private Integer tableNumber;
    private String location;
    private Double totalAmount;
    private String status;
    private LocalDateTime orderTime;

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.user = new UserDTO(order.getUser());
        this.orderType = order.getOrderType();
        this.tableNumber = order.getTableNumber();
        this.location = order.getLocation();
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus();
        this.orderTime = order.getOrderTime();
    }

    // Getters và Setters
}
