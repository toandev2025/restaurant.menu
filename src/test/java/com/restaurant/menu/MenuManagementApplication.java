package com.restaurant.menu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
public class MenuManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(MenuManagementApplication.class, args);
    }
}
