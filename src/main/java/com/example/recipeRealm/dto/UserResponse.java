package com.example.recipeRealm.dto;

import com.example.recipeRealm.model.Recipe;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    private Set<Recipe> recipes;
    private Set<Recipe> favoriteRecipes;
}

