package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final UserService userService;
    private final UserValidator userValidator;

    private static final String UPDATE_USER_URL = "/admin/update_user";

    @Autowired
    public AdminController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @GetMapping(value = "/admin_page")
    public String adminFullInfo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("users", userService.getAllUsers());
            return "admin/admin_page";
        }
        return "auth/login";
    }

    @GetMapping(value = "/edit")
    public String editUser(@ModelAttribute("userDto") UserDto userDto) {
        return UPDATE_USER_URL;
    }

    @PostMapping(value = "/update")
    public String updateUserExecution(@Valid @ModelAttribute("userDto") UserDto userDto,
                                      BindingResult result) {
        userValidator.validate(userDto, result);
        if (!userDto.getPassword().equals(userDto.getPasswordConfirm())) {
            result.rejectValue("passwordConfirm", "error.userDto",
                    "Passwords do not match!");
        }
        if (result.hasErrors()) {
            return UPDATE_USER_URL;
        }
        userService.updateUser(userService.convertToUser(userDto));
        return "redirect:/admin/admin_page";
    }

    @PostMapping(value = "/delete")
    public String deleteUser(@ModelAttribute("userDto") UserDto userDto, Model model) {
        Long userId = userDto.getId();
        User user = userService.getUserById(userId);
        boolean isAdmin = user.getRoles().stream()
                        .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            model.addAttribute("errorMessage", "You can not delete user with admin role!");
            return UPDATE_USER_URL;
        }
        userService.deleteUser(userId);
        return "redirect:/admin/admin_page";
    }
}
