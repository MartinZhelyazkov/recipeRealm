package com.example.recipeRealm.service;

import com.example.recipeRealm.dto.RecipeRequest;
import com.example.recipeRealm.dto.RecipeResponse;
import com.example.recipeRealm.model.Comment;
import com.example.recipeRealm.model.Recipe;

import java.util.Set;

public interface RecipeService {
    RecipeResponse addRecipe(RecipeRequest recipeRequest, Long userId);

    RecipeResponse findRecipe(Long recipeId);

    RecipeResponse updateRecipe(RecipeRequest recipeRequest, Long recipeId);

    void delRecipe(Long recipeId);

    Set<RecipeResponse> findAllRecipes();

    RecipeResponse likeRecipe(Long recipeId);

    RecipeResponse dislikeRecipe(Long recipeId);

    Set<Comment> getAllCommentsForRecipe(Long recipeId);

    Set<Recipe> findRecipesByTitleAndCategory(String title, String category);

    Set<RecipeResponse> getAllFavoriteRecipes(Long userId);
}
