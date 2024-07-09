package com.example.recipeRealm.converter;

import com.example.recipeRealm.dto.CommentRequest;
import com.example.recipeRealm.model.Comment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CommentConverter {
    public Comment toComment(CommentRequest commentRequest) {
        Comment comment = new Comment();
        comment.setText(commentRequest.getText());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setLikes(0L);
        comment.setDislikes(0L);
        return comment;
    }
}
