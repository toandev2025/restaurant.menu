package com.restaurant.menu.menu_management.Domain.DTO;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginDTO {
    @NotBlank(message = "username không được để trống")
    private String username;
    @NotBlank(message = "username không được để trống")
    private String password;
}
