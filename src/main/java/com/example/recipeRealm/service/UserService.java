package com.example.recipeRealm.service;

import com.example.recipeRealm.dto.UserRequest;
import com.example.recipeRealm.dto.UserResponse;
import com.example.recipeRealm.model.Recipe;

import java.util.Set;

public interface UserService {
    UserResponse registerUser(UserRequest userRequest);

    UserResponse findById(Long id);

    UserResponse updateUser(UserRequest userRequest, Long id);

    void delUser(Long id);

    Set<Recipe> getAllRecipeForUser(Long userId);

    UserResponse addToFavorite(Long recipeId, Long userId);

    Set<Recipe> findAllFavoriteRecipes(Long userId);

    UserResponse deleteFromFavorites(Long recipeId, Long userId);
}
