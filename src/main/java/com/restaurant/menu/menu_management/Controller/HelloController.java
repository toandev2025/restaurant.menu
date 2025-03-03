package com.restaurant.menu.menu_management.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String getHelloWorld() {
        return "Hello World (Hỏi Dân IT & Eric)";
    }
}
