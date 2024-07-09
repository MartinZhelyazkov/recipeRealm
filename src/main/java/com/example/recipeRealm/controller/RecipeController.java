package com.example.recipeRealm.controller;

import com.example.recipeRealm.dto.RecipeRequest;
import com.example.recipeRealm.dto.RecipeResponse;
import com.example.recipeRealm.model.Comment;
import com.example.recipeRealm.model.Recipe;
import com.example.recipeRealm.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path = "/api/v1/recipe")
public class RecipeController {
    @Autowired
    RecipeService recipeService;

    @PostMapping
    ResponseEntity<RecipeResponse> addRecipe(@Valid @RequestBody RecipeRequest recipeRequest, @RequestParam Long userId) {
        return new ResponseEntity<>(recipeService.addRecipe(recipeRequest, userId), HttpStatus.CREATED);
    }

    @GetMapping("{recipeId}")
    ResponseEntity<RecipeResponse> findRecipe(@PathVariable("recipeId") Long recipeId) {
        return new ResponseEntity<>(recipeService.findRecipe(recipeId), HttpStatus.FOUND);
    }

    @PutMapping("{id}")
    ResponseEntity<RecipeResponse> updateRecipe(@Valid @RequestBody RecipeRequest recipeRequest,
                                                @PathVariable("id") Long recipeId) {
        return new ResponseEntity<>(recipeService.updateRecipe(recipeRequest, recipeId), HttpStatus.OK);
    }

    @DeleteMapping
    void delRecipe(@RequestParam Long recipeId) {
        recipeService.delRecipe(recipeId);
    }

    @GetMapping
    ResponseEntity<Set<RecipeResponse>> findAllRecipes() {
        return new ResponseEntity<>(recipeService.findAllRecipes(), HttpStatus.FOUND);
    }

    @PutMapping(path = "/likes")
    ResponseEntity<RecipeResponse> likeRecipe(@RequestParam Long recipeId) {
        return new ResponseEntity<>(recipeService.likeRecipe(recipeId), HttpStatus.OK);
    }

    @PutMapping(path = "/dislikes")
    ResponseEntity<RecipeResponse> dislikeRecipe(@RequestParam Long recipeId) {
        return new ResponseEntity<>(recipeService.dislikeRecipe(recipeId), HttpStatus.OK);
    }

    @GetMapping(path = "/comments")
    ResponseEntity<Set<Comment>> getAllCommentsForRecipe(@RequestParam Long recipeId) {
        return new ResponseEntity<>(recipeService.getAllCommentsForRecipe(recipeId), HttpStatus.FOUND);
    }

    @GetMapping(path = "/search")
    ResponseEntity<Set<Recipe>> findRecipesByTitleAndCategory(@Valid @RequestParam String title, @RequestParam String category) {
        Set<Recipe> recipes = recipeService.findRecipesByTitleAndCategory(title, category);
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    @GetMapping(path = "/favorites")
    ResponseEntity<Set<RecipeResponse>> getAllFavoriteRecipes(@RequestParam Long userId) {
        return new ResponseEntity<>(recipeService.getAllFavoriteRecipes(userId), HttpStatus.FOUND);
    }
}
