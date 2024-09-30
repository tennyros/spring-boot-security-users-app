package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final UserValidator userValidator;

    private static final String UPDATE_USER_URL = "/admin/update_user";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ERROR = "errorMessage";

    @Autowired
    public AdminController(UserService userService, UserValidator userValidator, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
    }

    @GetMapping(value = "/admin_page")
    public String adminFullInfo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(ROLE_ADMIN))) {
            model.addAttribute("users", userService.getAllUsers());
            return "admin/admin_page";
        }
        return "auth/login";
    }

    @GetMapping(value = "/edit")
    public String editUser(@RequestParam("id") Long id, Model model) {
        User user = userService.getUserById(id);
        UserDto userDto = userService.convertToUserDto(user);

        userDto.setAdmin(user.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals(ROLE_ADMIN)));

        model.addAttribute("userDto", userDto);
        return UPDATE_USER_URL;
    }

    @PostMapping(value = "/update")
    public String updateUserExecution(@Valid @ModelAttribute("userDto") UserDto userDto,
                                      BindingResult result, Model model) {
        userValidator.validate(userDto, result);
        if (result.hasErrors()) {
            return UPDATE_USER_URL;
        }
        User existingUser = userService.getUserById(userDto.getId());
        if (userDto.getRoles() == null || userDto.getRoles().isEmpty()) {
            userDto.setRoles(existingUser.getRoles());
        }
        User user = userService.convertToUser(userDto);
        Role adminRole = roleService.getRoleByName(ROLE_ADMIN);

        if (userDto.getIsAdmin() && !userDto.getRoles().contains(adminRole)) {
            user.getRoles().add(adminRole);
        } else {
            user.getRoles().remove(adminRole);
        }
        userService.updateUser(user);
        return "redirect:/admin/admin_page";
    }

    @PostMapping(value = "/delete")
    public String deleteUser(@ModelAttribute("userDto") UserDto userDto, Model model) {
        Long userId = userDto.getId();
        User user = userService.getUserById(userId);
        boolean isAdmin = user.getRoles().stream()
                        .anyMatch(role -> role.getAuthority().equals(ROLE_ADMIN));
        if (isAdmin) {
            model.addAttribute(ERROR, "You can not delete user with administrator role!");
            return UPDATE_USER_URL;
        }
        try {
            userService.deleteUser(userId);
        } catch (UnsupportedOperationException e) {
            model.addAttribute(ERROR, e.getMessage());
            return "error_page";
        }
        return "redirect:/admin/admin_page";
    }
}
