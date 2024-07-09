package com.example.recipeRealm.controller;

import com.example.recipeRealm.dto.UserRequest;
import com.example.recipeRealm.dto.UserResponse;
import com.example.recipeRealm.model.Recipe;
import com.example.recipeRealm.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping
    ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest userRequest) {
        return new ResponseEntity<>(userService.registerUser(userRequest), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    ResponseEntity<UserResponse> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.FOUND);
    }

    @PutMapping("{id}")
    ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UserRequest userRequest,
                                            @PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.updateUser(userRequest, id), HttpStatus.OK);
    }

    @DeleteMapping
    void delUser(@RequestParam Long id) {
        userService.delUser(id);
    }

    @GetMapping(path = "/recipes")
    ResponseEntity<Set<Recipe>> getAllRecipeForUser(@RequestParam Long userId) {
        return new ResponseEntity<>(userService.getAllRecipeForUser(userId), HttpStatus.FOUND);
    }

    @PostMapping(path = "/favorites")
    ResponseEntity<UserResponse> addToFavorite(@RequestParam Long recipeId, @RequestParam Long userId) {
        return new ResponseEntity<>(userService.addToFavorite(recipeId, userId), HttpStatus.OK);
    }

    @GetMapping(path = "/favorites/{userId}")
    ResponseEntity<Set<Recipe>> findAllFavoriteRecipes(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(userService.findAllFavoriteRecipes(userId), HttpStatus.FOUND);
    }

    @DeleteMapping(path = "/favorites")
    ResponseEntity<UserResponse> deleteFromFavorites(@RequestParam Long recipeId, @RequestParam Long userId) {
        return new ResponseEntity<>(userService.deleteFromFavorites(recipeId, userId), HttpStatus.OK);
    }
}
