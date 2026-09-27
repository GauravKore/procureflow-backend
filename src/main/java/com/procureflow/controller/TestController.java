package com.procureflow.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/hello")
    public String hello(Authentication authentication) {
        return "Hello " + authentication.getName();
    }

    @GetMapping("/employee")
    public String employee() {
        return "Employee API accessed successfully";
    }

    @GetMapping("/manager")
    public String manager() {
        return "Manager API accessed successfully";
    }

    @GetMapping("/procurement")
    public String procurement() {
        return "Procurement API accessed successfully";
    }

    @GetMapping("/finance")
    public String finance() {
        return "Finance API accessed successfully";
    }
}