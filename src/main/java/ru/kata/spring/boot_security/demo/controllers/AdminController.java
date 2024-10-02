package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RegistrationService;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final RegistrationService registrationService;
    private final UserValidator userValidator;

    private static final String UPDATE_USER_URL = "/admin/update_user";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String REGISTRATION_URL = "/admin/registration";
    private static final String REDIRECT_ADMIN_PAGE = "redirect:/admin/admin_page";
    private static final String ERROR = "errorMessage";

    @Autowired
    public AdminController(UserService userService, RegistrationService registrationService, UserValidator userValidator, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
        this.registrationService = registrationService;
        this.userValidator = userValidator;
    }

    @GetMapping(value = "/admin_page")
    public String adminFullInfo(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "/admin/admin_page";
    }

    @GetMapping(value = "/registration")
    public String registration(@ModelAttribute("userDto") UserDto userDto, Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        userDto.setRoles(new HashSet<>(Collections.singletonList(roleService.getRoleByName("ROLE_USER"))));
        return REGISTRATION_URL;
    }

    @PostMapping(value = "/registration")
    public String registrationExecution(@Valid @ModelAttribute("userDto") UserDto userDto,
                                        BindingResult result, Model model) {
        userValidator.validate(userDto, result);
        if (result.hasErrors()) {
            model.addAttribute("roles", roleService.getAllRoles());
            return REGISTRATION_URL;
        }
        User user = userService.convertToUser(userDto);
        Set<Role> selectedRoles = userDto.getRoles();
        if (selectedRoles == null || selectedRoles.isEmpty()) {
            model.addAttribute("rolesError", "You must select a role to user!");
            return REGISTRATION_URL;
        }
        user.setRoles(selectedRoles);
        registrationService.register(user);
        return REDIRECT_ADMIN_PAGE;
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
                                      BindingResult result) {
        userValidator.validate(userDto, result);
        if (result.hasErrors()) {
            return UPDATE_USER_URL;
        }
        User existingUser = userService.getUserById(userDto.getId());
        if (userDto.getRoles() == null || userDto.getRoles().isEmpty()) {
            userDto.setRoles(existingUser.getRoles());
        }
        User user = userService.convertToUser(userDto);
        userService.updateUser(user);
        return REDIRECT_ADMIN_PAGE;
    }

    @PostMapping(value = "/delete")
    public String deleteUser(@ModelAttribute("userDto") UserDto userDto, Model model) {
        Long userId = userDto.getId();
        User user = userService.getUserById(userId);
        if (user.getId() == 1) {
            model.addAttribute(ERROR, "You can not delete super administrator!");
            return UPDATE_USER_URL;
        }
        try {
            userService.deleteUser(userId);
        } catch (UnsupportedOperationException e) {
            model.addAttribute(ERROR, e.getMessage());
            return "/error_page";
        }
        return REDIRECT_ADMIN_PAGE;
    }
}
