package ru.kata.spring.boot_security.demo.model;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @NotBlank(message = "Password must be defined!")
    @Column(name = "password", nullable = false)
    @Size(min = 6, max = 18, message = "Password must be from 6 to 18 symbols long!")
    private String password;

    @NotBlank(message = "First name must be defined!")
    @Size(min = 1, max = 30, message = "First name must be from 1 to 30 symbols long!")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name must be defined!")
    @Size(min = 1, max = 30, message = "Last name must be from 1 to 30 symbols long!")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank(message = "Email must be defined!")
    @Email(message = "Email must be valid!")
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull(message = "Age must be defined!")
    @Min(value = 14, message = "Age must not be less than 14!")
    @Max(value = 125, message = "Age must not be more than 125!")
    @Column(name = "age", nullable = false)
    private Integer age;

    public User() {}

    public User(String firstName, String lastName, String email, Integer age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "Password must be defined!") @Size(min = 6, max = 18, message = "Password must be from 6 to 18 symbols long!") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password must be defined!") @Size(min = 6, max = 18, message = "Password must be from 6 to 18 symbols long!") String password) {
        this.password = password;
    }

    public @NotBlank(message = "First name must be defined!") @Size(min = 2, max = 30, message = "First name is too short or too long!") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank(message = "First name must be defined!") @Size(min = 2, max = 30, message = "First name is too short or too long!") String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank(message = "Last name must be defined!") @Size(min = 2, max = 30, message = "Last name is too short or too long!") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank(message = "Last name must be defined!") @Size(min = 2, max = 30, message = "Last name is too short or too long!") String lastName) {
        this.lastName = lastName;
    }

    public @NotBlank(message = "Email must be defined!") @Email(message = "Email must be valid!") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email must be defined!") @Email(message = "Email must be valid!") String email) {
        this.email = email;
    }

    public @NotNull(message = "Age must be defined!") @Min(value = 14, message = "Age must not be less than 14!") @Max(value = 125, message = "Age must not be more than 125!") Integer getAge() {
        return age;
    }

    public void setAge(@NotNull(message = "Age must be defined!") @Min(value = 14, message = "Age must not be less than 14!") @Max(value = 125, message = "Age must not be more than 125!") Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
