package com.example.recipeRealm.controller;

import com.example.recipeRealm.dto.CommentRequest;
import com.example.recipeRealm.dto.CommentResponse;
import com.example.recipeRealm.model.Comment;
import com.example.recipeRealm.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path = "/api/v1/comment")
@Tag(name = "Comment API ", description = "API for managing comments")
public class CommentController {
    @Autowired
    CommentService commentService;

    @PostMapping
    @Operation(summary = "Creating comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment successfully added"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "400", description = "Bad request"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Comment.class)))
    })
    ResponseEntity<CommentResponse> addComment(@Valid @RequestBody CommentRequest commentRequest, @RequestParam Long recipeId) {
        return new ResponseEntity<>(commentService.addComment(commentRequest, recipeId), HttpStatus.CREATED);
    }

    @Operation(summary = "Finding all comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "All comments successfully found"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Comment.class)))})

    @GetMapping
    ResponseEntity<Set<CommentResponse>> findAllCommentsForRecipe(@RequestParam Long recipeId) {
        return new ResponseEntity<>(commentService.findAllCommentsForRecipe(recipeId), HttpStatus.FOUND);
    }

    @Operation(summary = "Deleting comment by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment successfully deleted"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Comment.class)))})
    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    void delComment(@RequestParam Long commentId) {
        commentService.delComment(commentId);
    }

    @Operation(summary = "Liking comment by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment liked"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Comment.class)))})
    @PutMapping(path = "/likes")
    ResponseEntity<CommentResponse> likeComment(@RequestParam Long commentId) {
        return new ResponseEntity<>(commentService.likeComment(commentId), HttpStatus.OK);
    }

    @Operation(summary = "Disliking comment by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment disliked"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Comment.class)))})
    @PutMapping(path = "/dislikes")
    ResponseEntity<CommentResponse> dislikeComment(@RequestParam Long commentId) {
        return new ResponseEntity<>(commentService.dislikeComment(commentId), HttpStatus.OK);
    }
}
