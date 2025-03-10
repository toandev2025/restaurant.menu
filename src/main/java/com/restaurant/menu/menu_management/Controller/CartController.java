package com.restaurant.menu.menu_management.Controller;

import com.restaurant.menu.menu_management.Domain.DTO.CartDTO;
import com.restaurant.menu.menu_management.Domain.DTO.OrderDTO;
import com.restaurant.menu.menu_management.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO.CartResponse> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(this.cartService.getCart(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<CartDTO.CartResponse> addToCart(@RequestBody CartDTO.AddToCartRequest request) {
        return ResponseEntity.ok(this.cartService.addToCart(
                request.getUserId(), request.getDishId(), request.getQuantity(), request.getNote()));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeFromCart(@RequestBody CartDTO.RemoveFromCartRequest request) {
        this.cartService.removeFromCart(request.getUserId(), request.getCartItemId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderDTO> checkout(@RequestBody CartDTO.CheckoutRequest request) {
        return ResponseEntity.ok(cartService.checkout(
                request.getCartId(),
                request.getUserId(),
                request.getOrderType(),
                request.getLocation(),
                request.getTableNumber(),
                request.getNote(),
                request.getPhoneNumber()));
    }

}
