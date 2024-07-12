package com.example.recipeRealm.converter;

import com.example.recipeRealm.dto.CommentRequest;
import com.example.recipeRealm.model.Comment;
import com.example.recipeRealm.service.impl.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CommentConverter {
    private final CurrentUserService currentUserService;

    @Autowired
    public CommentConverter(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    public Comment toComment(CommentRequest commentRequest) {
        Comment comment = new Comment();
        comment.setText(commentRequest.getText());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setLikes(0L);
        comment.setDislikes(0L);
        comment.setAuthor(currentUserService.extractCurrentUser());
        return comment;
    }
}
