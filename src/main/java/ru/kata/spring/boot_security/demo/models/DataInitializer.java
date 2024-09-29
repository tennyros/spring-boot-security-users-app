package ru.kata.spring.boot_security.demo.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.age}")
    private Integer adminAge;

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        createRoles("ROLE_ADMIN");
        createRoles("ROLE_USER");
        createAdmin(adminUsername, adminPassword, adminEmail, adminAge);
    }

    private void createRoles(String roleName) {
        if (roleRepository.findByRoleName(roleName) == null) {
            roleRepository.save(new Role(roleName));
        }
    }

    private void createAdmin(String username, String password, String email, Integer age) {
        if (userRepository.findByUsername(username).isEmpty()) {
            User admin = new User();
            admin.setUsername(username);
            admin.setPassword(passwordEncoder.encode(password));
            admin.setEmail(email);
            admin.setAge(age);
            admin.setRoles(Set.of(roleRepository.findByRoleName("ROLE_ADMIN"),
                    roleRepository.findByRoleName("ROLE_USER")));
            admin.setAdmin(true);
            userRepository.save(admin);
        }
    }
}
