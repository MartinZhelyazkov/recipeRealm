package com.example.recipeRealm.dto;

import com.example.recipeRealm.model.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class RecipeResponse {
    private Long id;
    private String title;
    private String description;
    private String ingredients;
    private String instructions;
    private String category;
    private LocalDateTime createdAt;
    private Long likes;
    private Long dislikes;
    private Set<Comment> comments;
}
