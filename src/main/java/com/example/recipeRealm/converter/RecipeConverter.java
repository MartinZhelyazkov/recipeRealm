package com.example.recipeRealm.converter;

import com.example.recipeRealm.dto.RecipeRequest;
import com.example.recipeRealm.model.Recipe;
import com.example.recipeRealm.service.impl.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RecipeConverter {
    private final CurrentUserService currentUserService;

    @Autowired
    public RecipeConverter(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

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
        recipe.setAuthor(currentUserService.extractCurrentUser());
        return recipe;
    }
}
