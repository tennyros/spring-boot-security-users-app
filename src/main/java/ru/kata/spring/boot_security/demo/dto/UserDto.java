package ru.kata.spring.boot_security.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kata.spring.boot_security.demo.models.Role;

import javax.validation.constraints.*;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

    private Long id;

    @NotBlank(message = "Username must be defined!")
    @Size(min = 3, max = 18, message = "Username must be from 3 to 18 symbols long!")
    private String username;

    @NotBlank(message = "Password must be defined!")
    @Size(min = 6, max = 18, message = "Password must be from 6 to 18 symbols long!")
    private String password;

    @NotBlank(message = "Please, confirm your password!")
    private String passwordConfirm;

    @NotBlank(message = "Email must be defined!")
    @Email(message = "Email must be valid!")
    private String email;

    @NotNull(message = "Age must be defined!")
    @Min(value = 14, message = "Age must not be less than 14!")
    @Max(value = 125, message = "Age must not be more than 125!")
    private Integer age;

    private Set<Role> roles;

    private boolean isAdmin;

    public boolean getIsAdmin() {
        return isAdmin;
    }
}
