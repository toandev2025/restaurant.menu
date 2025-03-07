package com.restaurant.menu.menu_management.Domain;

import java.time.LocalDateTime;

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
    private Double totalPrice;
    private String status;
    private LocalDateTime orderTime;

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.user = new UserDTO(order.getUser());
        this.orderType = order.getOrderType();
        this.tableNumber = order.getTableNumber();
        this.location = order.getLocation();
        this.totalPrice = order.getTotalPrice();
        this.status = order.getStatus();
        this.orderTime = order.getOrderTime();
    }

    // Getters và Setters
}
