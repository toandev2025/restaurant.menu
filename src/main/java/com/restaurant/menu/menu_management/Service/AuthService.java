package com.restaurant.menu.menu_management.Service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.restaurant.menu.menu_management.Domain.User;
import com.restaurant.menu.menu_management.Service.User.UserService;

@Service
public class AuthService {
    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt)) {
            throw new IllegalStateException("Không thể xác thực người dùng");
        }
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getSubject(); // Lấy thông tin user từ subject của JWT

        // Gọi UserService để lấy user
        User user = userService.handleGetUserByUsername(username);
        // Kiểm tra null thủ công
        if (user == null) {
            throw new IllegalArgumentException("Người dùng không tồn tại");
        }
        return user;
    }
}