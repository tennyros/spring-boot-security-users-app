package ru.kata.spring.boot_security.demo.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

import java.util.Set;

@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @NotBlank(message = "Username must be defined!")
    @Size(min = 3, max = 18, message = "Username must be from 3 to 18 symbols long!")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Password must be defined!")
    @Size(min = 6, max = 18, message = "Password must be from 6 to 18 symbols long!")
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank(message = "Please, confirm your password!")
    @Transient
    private String passwordConfirm;

    @NotBlank(message = "Email must be defined!")
    @Email(message = "Email must be valid!")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull(message = "Age must be defined!")
    @Min(value = 14, message = "Age must not be less than 14!")
    @Max(value = 125, message = "Age must not be more than 125!")
    @Column(name = "age", nullable = false)
    private Integer age;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    public User() {}

    public User(String username, String password, String email, Integer age) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "age=" + age +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", id=" + id +
                ", roles=" + roles +
                '}';
    }
}
