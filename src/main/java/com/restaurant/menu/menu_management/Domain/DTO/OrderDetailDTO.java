package com.restaurant.menu.menu_management.Domain.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
public class OrderDetailDTO {
    private Long orderId;
    private List<Item> items;
    private Summary summary;

    // Nested static class cho OrderDetailItemDto
    @Getter
    @Setter
    public static class Item {
        private Long orderDetail_id;
        private Dish dish;
        private Integer quantity;
        private Double unitPrice;
        private Double subtotal;
        private String note;
        private String status;
    }

    // Nested static class cho DishDto
    @Getter
    @Setter
    public static class Dish {
        private Long id;
        private String name;
        private Double price;
        private String imageUrl;
    }

    // Nested static class cho OrderSummaryDto
    @Getter
    @Setter
    public static class Summary {
        private Integer totalItems;
        private Double totalAmount;
    }
}
