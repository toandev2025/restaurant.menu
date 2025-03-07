package com.restaurant.menu.menu_management.Domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String roleName;

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.roleName = user.getRole().getName();
    }

    // Getters và Setters
}
