package com.example.recipeRealm.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponse {
    private Long id;
    private String text;
    private LocalDateTime createdAt;
    private Long likes;
    private Long dislikes;
}
