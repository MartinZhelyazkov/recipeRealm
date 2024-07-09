package com.example.recipeRealm.service;

import com.example.recipeRealm.dto.CommentRequest;
import com.example.recipeRealm.dto.CommentResponse;

import java.util.Set;

public interface CommentService {
    CommentResponse addComment(CommentRequest commentRequest, Long recipeId);

    Set<CommentResponse> findAllCommentsForRecipe(Long recipeId);

    void delComment(Long commentId);

    CommentResponse likeComment(Long commentId);

    CommentResponse dislikeComment(Long commentId);

}
