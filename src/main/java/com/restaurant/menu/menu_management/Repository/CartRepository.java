package com.restaurant.menu.menu_management.Repository;

import com.restaurant.menu.menu_management.Domain.Cart;
import com.restaurant.menu.menu_management.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
