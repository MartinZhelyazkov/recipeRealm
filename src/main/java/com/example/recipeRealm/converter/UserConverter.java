package com.example.recipeRealm.converter;

import com.example.recipeRealm.dto.UserRequest;
import com.example.recipeRealm.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserConverter {
    public User toUser(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setCreatedAt(LocalDateTime.now());
        //user.setPassword(passwordEncoder.setPassword(userRequest.getPassword)
        return user;
    }
}
