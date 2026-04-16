package com.grocery.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Authentication authentication) {
        boolean isEmployee = authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"));
        return isEmployee ? "redirect:/admin/inventory" : "redirect:/cart/browse";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
