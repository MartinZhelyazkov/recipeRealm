package com.example.recipeRealm.controller;

import com.example.recipeRealm.dto.CommentRequest;
import com.example.recipeRealm.dto.CommentResponse;
import com.example.recipeRealm.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path = "/api/v1/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @PostMapping
    ResponseEntity<CommentResponse> addComment(@Valid @RequestBody CommentRequest commentRequest, @RequestParam Long recipeId) {
        return new ResponseEntity<>(commentService.addComment(commentRequest, recipeId), HttpStatus.CREATED);
    }

    @GetMapping
    ResponseEntity<Set<CommentResponse>> findAllCommentsForRecipe(@RequestParam Long recipeId) {
        return new ResponseEntity<>(commentService.findAllCommentsForRecipe(recipeId), HttpStatus.FOUND);
    }

    @DeleteMapping
    public void delComment(@RequestParam Long commentId) {
        commentService.delComment(commentId);
    }

    @PutMapping(path = "/likes")
    public ResponseEntity<CommentResponse> likeComment(@RequestParam Long commentId) {
        CommentResponse response = commentService.likeComment(commentId);
        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/dislikes")
    ResponseEntity<CommentResponse> dislikeComment(@RequestParam Long commentId) {
        return new ResponseEntity<>(commentService.dislikeComment(commentId), HttpStatus.OK);
    }
}
