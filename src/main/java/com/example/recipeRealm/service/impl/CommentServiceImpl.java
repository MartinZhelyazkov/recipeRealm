package com.example.recipeRealm.service.impl;

import com.example.recipeRealm.advice.exceptions.RecordNotFoundException;
import com.example.recipeRealm.converter.CommentConverter;
import com.example.recipeRealm.dto.CommentRequest;
import com.example.recipeRealm.dto.CommentResponse;
import com.example.recipeRealm.model.Comment;
import com.example.recipeRealm.model.Recipe;
import com.example.recipeRealm.repository.CommentRepository;
import com.example.recipeRealm.repository.RecipeRepository;
import com.example.recipeRealm.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final CommentConverter commentConverter;

    private final RecipeRepository recipeRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, CommentConverter commentConverter, RecipeRepository recipeRepository) {
        this.commentRepository = commentRepository;
        this.commentConverter = commentConverter;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public CommentResponse addComment(CommentRequest commentRequest, Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Recipe with id %s not found", recipeId)));
        Comment comment = commentConverter.toComment(commentRequest);
        comment.setRecipe(recipe);
        Comment saveComment = commentRepository.save(comment);
        CommentResponse commentResponse = new CommentResponse();
        BeanUtils.copyProperties(saveComment, commentResponse);
        return commentResponse;
    }

    @Override
    public Set<CommentResponse> findAllCommentsForRecipe(Long recipeId) {
        List<Comment> comments = commentRepository.findByRecipeId(recipeId);
        Set<CommentResponse> commentResponsesSet = new HashSet<>();
        for (Comment comment : comments) {
            CommentResponse commentResponse = new CommentResponse();
            BeanUtils.copyProperties(comment, commentResponse);
            commentResponsesSet.add(commentResponse);
        }
        return commentResponsesSet;
    }

    @Override
    public void delComment(Long commentId) {
        commentRepository.findById(commentId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Comment with id %s not found", commentId)));
        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentResponse likeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Comment with id %s not found", commentId)));
        comment.setLikes(comment.getLikes() + 1);
        commentRepository.save(comment);
        CommentResponse commentResponse = new CommentResponse();
        BeanUtils.copyProperties(comment, commentResponse);

        return commentResponse;
    }

    @Override
    public CommentResponse dislikeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Comment with id %s not found", commentId)));
        comment.setDislikes(comment.getDislikes() + 1);
        commentRepository.save(comment);
        CommentResponse commentResponse = new CommentResponse();
        BeanUtils.copyProperties(comment, commentResponse);
        return commentResponse;
    }
}
