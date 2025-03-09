package com.restaurant.menu.menu_management.Domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class CartDTO {

    /** DTO cho request thêm món vào giỏ hàng */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddToCartRequest {
        private Long userId;
        private Long dishId;
        private int quantity;
        private String note;
    }

    /** DTO cho request xóa món khỏi giỏ hàng */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RemoveFromCartRequest {
        private Long userId;
        private Long cartItemId;
    }

    /** DTO cho request checkout */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckoutRequest {
        private Long cartId; // 🆕 Thêm cartId để xác định giỏ hàng
        private Long userId;
        private String orderType; // "DINE_IN" hoặc "TAKEAWAY"
        private String location; // Nếu orderType là "TAKEAWAY"
        private Integer tableNumber;// Nếu orderType là "DINE_IN"
    }

    /** DTO cho phản hồi giỏ hàng */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartResponse {
        private Long id;
        private UserDTO user;
        private List<CartItemDTO> cartItems;
    }

    /** DTO cho từng mục trong giỏ hàng */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItemDTO {
        private Long id;
        private DishDTO dish;
        private Integer quantity;
        private Double unitPrice;
        private Double subtotal;
        private String note;
    }

    /** DTO cho món ăn */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DishDTO {
        private Long id;
        private String name;
        private Double price;
        private String imageUrl;
    }

    /** DTO cho thông tin người dùng */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDTO {
        private Long id;
        private String name;
        private String email;
        private String roleName;
    }
}
