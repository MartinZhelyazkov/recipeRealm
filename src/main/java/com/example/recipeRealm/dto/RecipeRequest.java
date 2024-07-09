package com.example.recipeRealm.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeRequest {
    @NotNull(message = "Title can't be null")
    @Size(min = 2, max = 50, message = "Title must be between 2 and 50 characters")
    private String title;
    @NotNull(message = "Description can't be null")
    @Size(min = 2, max = 100, message = "Description must be between 2 and 100 characters")
    private String description;
    @NotNull(message = "Ingredients can't be null")
    @Size(min = 2, max = 150, message = "Ingredients must be between 2 and 150 characters")
    private String ingredients;
    @NotNull(message = "Instructions can't be null")
    @Size(min = 2, max = 255, message = "Instructions must be between 2 and 255 characters")
    private String instructions;
    @NotNull(message = "Category can't be null")
    @Size(min = 2, max = 50, message = "Category must be between 2 and 50 characters")
    private String category;
}
