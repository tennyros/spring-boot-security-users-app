package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/auth")
public class LoginController {

    @GetMapping(value = "/login")
    public String login() {
        return "auth/login";
    }
}
