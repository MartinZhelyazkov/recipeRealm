package com.example.recipeRealm.service.impl;

import com.example.recipeRealm.advice.exceptions.EmailAlreadyExistException;
import com.example.recipeRealm.advice.exceptions.RecordNotFoundException;
import com.example.recipeRealm.converter.UserConverter;
import com.example.recipeRealm.dto.UserRequest;
import com.example.recipeRealm.dto.UserResponse;
import com.example.recipeRealm.model.Recipe;
import com.example.recipeRealm.model.User;
import com.example.recipeRealm.repository.RecipeRepository;
import com.example.recipeRealm.repository.UserRepository;
import com.example.recipeRealm.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserConverter userConverter;

    private final UserRepository userRepository;

    private final RecipeRepository recipeRepository;

    @Autowired
    public UserServiceImpl(UserConverter userConverter, UserRepository userRepository, RecipeRepository recipeRepository) {
        this.userConverter = userConverter;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public UserResponse registerUser(UserRequest userRequest) {
        Optional<User> existingUser = userRepository.findByEmail(userRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new EmailAlreadyExistException(String.format("This email %s is already taken", userRequest.getEmail()));
        } else {
            User user = userConverter.toUser(userRequest);
            User savedUser = userRepository.save(user);
            UserResponse userResponse = new UserResponse();
            BeanUtils.copyProperties(savedUser, userResponse);
            return userResponse;
        }
    }

    @Override
    public UserResponse findById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RecordNotFoundException(String.format("User with id %S not found", userId)));
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        return userResponse;
    }

    @Override
    public UserResponse updateUser(UserRequest userRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RecordNotFoundException(String.format("User with id %S not found", userId)));
        Optional<User> existingUser = userRepository.findByEmail(userRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new EmailAlreadyExistException(String.format("This email %s is already taken", userRequest.getEmail()));
        } else {
            user.setEmail(userRequest.getEmail());
            user.setUsername(userRequest.getUsername());
            User savedUser = userRepository.save(user);
            UserResponse userResponse = new UserResponse();
            BeanUtils.copyProperties(savedUser, userResponse);
            return userResponse;
        }
    }

    @Override
    public void delUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Recipe with id %s not found", userId)));
        userRepository.deleteById(userId);
    }

    @Override
    public Set<Recipe> getAllRecipeForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("User with id %s not found", userId)));
        return user.getRecipes();
    }

    @Override
    public UserResponse addToFavorite(Long recipeId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("User with id %s not found", userId)));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Recipe with id %s not found", recipeId)));
        user.getFavoriteRecipes().add(recipe);
        userRepository.save(user);
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        return userResponse;
    }

    @Override
    public Set<Recipe> findAllFavoriteRecipes(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("User with id %s not found", userId)));
        return user.getFavoriteRecipes();
    }

    @Override
    public UserResponse deleteFromFavorites(Long recipeId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("User with id %s not found", userId)));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Recipe with id %s not found", recipeId)));
        user.getFavoriteRecipes().remove(recipe);
        userRepository.save(user);
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        return userResponse;
    }
}
