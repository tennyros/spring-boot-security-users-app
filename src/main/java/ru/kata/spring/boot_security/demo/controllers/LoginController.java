package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

@Controller
@RequestMapping(value = "/auth")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/login")
    public String login() {
        System.out.println(userService.getUserByEmail("admin@mail.com"));
        System.out.println(userService.getUserById(1L).getUsername());
        return "auth/login";
    }
}
