package ru.kata.spring.boot_security.demo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (id == 1) {
            log.error("You are trying to delete a user with id {} (super administrator)!", id);
            throw new UnsupportedOperationException("You can not delete super administrator!");
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAllUsersWithRole();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User convertToUser(UserDto userDto) {
        User user;
        if (userDto.getId() != null) {
            user = userRepository.findById(userDto.getId())
                    .orElseThrow(() -> {
                        log.error("User with ID {} not found!", userDto.getId());
                        return new IllegalArgumentException("User with ID " + userDto.getId() + " not found!");
                    });
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        } else {
            user = new User();
            user.setPassword(userDto.getPassword());
        }
        user.setId(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setAge(userDto.getAge());
        user.setAdmin(userDto.getIsAdmin());
        user.setRoles(userDto.getRoles());
        return user;
    }

    @Override
    public UserDto convertToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPassword(passwordEncoder.encode(user.getPassword()));
        userDto.setEmail(user.getEmail());
        userDto.setAge(user.getAge());
        userDto.setRoles(user.getRoles());
        return userDto;
    }
}
