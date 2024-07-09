package com.example.recipeRealm.converter;

import com.example.recipeRealm.dto.RecipeRequest;
import com.example.recipeRealm.model.Recipe;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RecipeConverter {
    public Recipe toRecipe(RecipeRequest recipeRequest) {
        Recipe recipe = new Recipe();
        recipe.setTitle(recipeRequest.getTitle());
        recipe.setDescription(recipeRequest.getDescription());
        recipe.setIngredients(recipeRequest.getIngredients());
        recipe.setInstructions(recipeRequest.getInstructions());
        recipe.setCategory(recipeRequest.getCategory());
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setLikes(0L);
        recipe.setDislikes(0L);
        return recipe;
    }
}
