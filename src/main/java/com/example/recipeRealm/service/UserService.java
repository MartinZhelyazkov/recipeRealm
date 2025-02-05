package com.example.recipeRealm.service;

import com.example.recipeRealm.dto.LoginRequest;
import com.example.recipeRealm.dto.UserRequest;
import com.example.recipeRealm.dto.UserResponse;
import com.example.recipeRealm.model.Recipe;
import com.example.recipeRealm.model.User;

import java.util.Set;

public interface UserService {
    UserResponse registerUser(UserRequest userRequest);

    UserResponse findById(Long userId);

    UserResponse updateUser(UserRequest userRequest, Long userId);

    void delUser(Long userId);

    Set<Recipe> getAllRecipeForUser(Long userId);

    UserResponse addToFavorite(Long recipeId, Long userId);

    Set<Recipe> findAllFavoriteRecipes(Long userId);

    UserResponse deleteFromFavorites(Long recipeId, Long userId);

    String login(LoginRequest loginRequest);

    User findByEmail(String email);
}
