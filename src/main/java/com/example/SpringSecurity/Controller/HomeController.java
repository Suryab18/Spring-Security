package com.example.SpringSecurity.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/admin-login")
    public String adminLogin() {
        return "Please login admin!";
    }
}
