package com.restaurant.menu.menu_management.Domain.DTO;

import java.time.LocalDateTime;
import java.util.List;

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
    private String phoneNumber;
    private String paymentMethod;
    private String note;
    private Double totalAmount;
    private String status;
    private LocalDateTime orderTime;
    private OrderDetailDTO orderDetailsDTO;

    public OrderDTO(Order order, OrderDetailDTO orderDetailDTO) {
        this.id = order.getId();
        this.user = new UserDTO(order.getUser());
        this.orderType = order.getOrderType();
        this.tableNumber = order.getTableNumber();
        this.location = order.getLocation();
        this.phoneNumber = order.getPhoneNumber();
        this.paymentMethod = order.getPaymentMethod();
        this.note = order.getNote();
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus();
        this.orderTime = order.getOrderTime();
        this.orderDetailsDTO = orderDetailDTO;
    }

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.user = new UserDTO(order.getUser());
        this.orderType = order.getOrderType();
        this.tableNumber = order.getTableNumber();
        this.location = order.getLocation();
        this.phoneNumber = order.getPhoneNumber();
        this.paymentMethod = order.getPaymentMethod();
        this.note = order.getNote();
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus();
        this.orderTime = order.getOrderTime();
    }

    // Getters và Setters
}
