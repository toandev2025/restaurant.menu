package com.restaurant.menu.menu_management.Repository;

import com.restaurant.menu.menu_management.Domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
