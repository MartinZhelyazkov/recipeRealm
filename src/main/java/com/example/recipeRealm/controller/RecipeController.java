package com.example.recipeRealm.controller;

import com.example.recipeRealm.dto.RecipeRequest;
import com.example.recipeRealm.dto.RecipeResponse;
import com.example.recipeRealm.model.Comment;
import com.example.recipeRealm.model.Recipe;
import com.example.recipeRealm.service.RecipeService;
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
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path = "/api/v1/recipe")
@Tag(name = "Recipe API ", description = "API for managing recipes")
public class RecipeController {
    @Autowired
    RecipeService recipeService;

    @Operation(summary = "Creating recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recipe successfully added"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Recipe.class))),
            @ApiResponse(responseCode = "400", description = "Bad request"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Recipe.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Recipe.class)))
    })
    @PostMapping
    ResponseEntity<RecipeResponse> addRecipe(@Valid @RequestBody RecipeRequest recipeRequest, @RequestParam Long userId) {
        return new ResponseEntity<>(recipeService.addRecipe(recipeRequest, userId), HttpStatus.CREATED);
    }

    @Operation(summary = "Finding recipe by ID ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Recipe successfully found"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Recipe.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Recipe.class)))})

    @GetMapping("{recipeId}")
    ResponseEntity<RecipeResponse> findRecipe(@PathVariable("recipeId") Long recipeId) {
        return new ResponseEntity<>(recipeService.findRecipe(recipeId), HttpStatus.FOUND);
    }

    @PutMapping("{id}")
    @Operation(summary = "Updating recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe successfully updated"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Recipe.class))),
            @ApiResponse(responseCode = "400", description = "Bad request"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Recipe.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Recipe.class)))
    })
    ResponseEntity<RecipeResponse> updateRecipe(@Valid @RequestBody RecipeRequest recipeRequest,
                                                @PathVariable("id") Long recipeId) {
        return new ResponseEntity<>(recipeService.updateRecipe(recipeRequest, recipeId), HttpStatus.OK);
    }

    @Operation(summary = "Deleting recipe by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe successfully deleted"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Recipe.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Recipe.class)))})
    @DeleteMapping
    void delRecipe(@RequestParam Long recipeId) {
        recipeService.delRecipe(recipeId);
    }

    @Operation(summary = "Finding all recipes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "All recipes successfully found"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Recipe.class)))})
    @GetMapping
    ResponseEntity<Set<RecipeResponse>> findAllRecipes() {
        return new ResponseEntity<>(recipeService.findAllRecipes(), HttpStatus.FOUND);
    }

    @Operation(summary = "Liking recipe by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe liked"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Recipe.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Recipe.class)))})
    @PutMapping(path = "/likes")
    ResponseEntity<RecipeResponse> likeRecipe(@RequestParam Long recipeId) {
        return new ResponseEntity<>(recipeService.likeRecipe(recipeId), HttpStatus.OK);
    }

    @Operation(summary = "Disliking recipe by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe disliked"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Recipe.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Recipe.class)))})
    @PutMapping(path = "/dislikes")
    ResponseEntity<RecipeResponse> dislikeRecipe(@RequestParam Long recipeId) {
        return new ResponseEntity<>(recipeService.dislikeRecipe(recipeId), HttpStatus.OK);
    }

    @GetMapping(path = "/comments")
    @Operation(summary = "Finding all comments for recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "All comments for recipe successfully found"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Recipe.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Recipe.class)))})
    ResponseEntity<Set<Comment>> getAllCommentsForRecipe(@RequestParam Long recipeId) {
        return new ResponseEntity<>(recipeService.getAllCommentsForRecipe(recipeId), HttpStatus.FOUND);
    }

    @Operation(summary = "Finding all recipes by searching")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Search successful"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Recipe.class)))})
    @GetMapping(path = "/search")
    ResponseEntity<Set<Recipe>> findRecipesByTitleAndCategory(@RequestParam String title, @RequestParam String category) {
        Set<Recipe> recipes = recipeService.findRecipesByTitleAndCategory(title, category);
        return new ResponseEntity<>(recipes, HttpStatus.FOUND);
    }

    @Operation(summary = "Finding all favorite recipes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "All favorite recipes successfully found"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Recipe.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"
                    , content = @Content(mediaType = "application/json"
                    , schema = @Schema(implementation = Recipe.class)))})
    @GetMapping(path = "/favorites")
    ResponseEntity<Set<RecipeResponse>> getAllFavoriteRecipes(@RequestParam Long userId) {
        return new ResponseEntity<>(recipeService.getAllFavoriteRecipes(userId), HttpStatus.FOUND);
    }
}
