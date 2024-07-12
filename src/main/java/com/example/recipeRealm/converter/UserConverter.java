package com.example.recipeRealm.converter;

import com.example.recipeRealm.dto.UserRequest;
import com.example.recipeRealm.enums.Role;
import com.example.recipeRealm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserConverter {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserConverter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User toUser(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        return user;
    }
}
