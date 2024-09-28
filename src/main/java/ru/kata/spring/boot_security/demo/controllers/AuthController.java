package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.services.RegistrationService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/auth")
public class AuthController {

    private final RegistrationService registrationService;
    private final UserValidator userValidator;
    private final UserService userService;

    @Autowired
    public AuthController(RegistrationService registrationService, UserValidator userValidator,
                          UserService userService) {
        this.registrationService = registrationService;
        this.userValidator = userValidator;
        this.userService = userService;
    }

    @GetMapping(value = "/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping(value = "/registration")
    public String registration(@ModelAttribute("userDto") UserDto userDto) {
        return "auth/registration";
    }

    @PostMapping(value = "/registration")
    public String registrationExecution(@Valid @ModelAttribute("userDto") UserDto userDto,
                                        BindingResult result) {
        userValidator.validate(userDto, result);
        if (!userDto.getPassword().equals(userDto.getPasswordConfirm())) {
            result.rejectValue("passwordConfirm", "error.userDto",
                    "Passwords do not match!");
        }
        if (result.hasErrors()) {
            return "auth/registration";
        }
        registrationService.register(userService.convertToUser(userDto));
        return "redirect:/auth/login";
    }
}
