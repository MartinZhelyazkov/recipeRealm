package com.example.recipeRealm.controller;

import com.example.recipeRealm.dto.LoginRequest;
import com.example.recipeRealm.dto.UserRequest;
import com.example.recipeRealm.dto.UserResponse;
import com.example.recipeRealm.model.Recipe;
import com.example.recipeRealm.model.User;
import com.example.recipeRealm.service.UserService;
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
@RequestMapping(path = "/api/v1/user")
@Tag(name = "User API ", description = "API for managing users")
public class UserController {
    @Autowired
    UserService userService;

    @Operation(summary = "Creating user")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "User successfully added", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))), @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))})
    @PostMapping(path = "/register")
    ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest userRequest) {
        return new ResponseEntity<>(userService.registerUser(userRequest), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    @Operation(summary = "Finding user by ID ")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiResponses(value = {@ApiResponse(responseCode = "302", description = "User successfully found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))), @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))})
    ResponseEntity<UserResponse> findById(@PathVariable("id") Long userId) {
        return new ResponseEntity<>(userService.findById(userId), HttpStatus.FOUND);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Updating user")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User successfully updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))), @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))})
    ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UserRequest userRequest, @PathVariable("id") Long userId) {
        return new ResponseEntity<>(userService.updateUser(userRequest, userId), HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Deleting user by ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User successfully deleted", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))), @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))})
    void delUser(@RequestParam Long id) {
        userService.delUser(id);
    }

    @Operation(summary = "Finding all recipes for user")
    @ApiResponses(value = {@ApiResponse(responseCode = "302", description = "All recipes successfully found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))), @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))})
    @GetMapping(path = "/recipes")
    ResponseEntity<Set<Recipe>> getAllRecipeForUser(@RequestParam Long userId) {
        return new ResponseEntity<>(userService.getAllRecipeForUser(userId), HttpStatus.FOUND);
    }

    @Operation(summary = "Add recipe to favorite for user")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Recipe successfully added", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))), @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))})
    @PostMapping(path = "/favorites")
    ResponseEntity<UserResponse> addToFavorite(@RequestParam Long recipeId, @RequestParam Long userId) {
        return new ResponseEntity<>(userService.addToFavorite(recipeId, userId), HttpStatus.OK);
    }

    @Operation(summary = "Find all favorite recipes")
    @ApiResponses(value = {@ApiResponse(responseCode = "302", description = "All favorite recipes successfully found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))), @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))})
    @GetMapping(path = "/favorites/{userId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<Set<Recipe>> findAllFavoriteRecipes(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(userService.findAllFavoriteRecipes(userId), HttpStatus.FOUND);
    }

    @Operation(summary = "Delete from favorite recipes")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "A recipe was successfully deleted", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))), @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))})
    @DeleteMapping(path = "/favorites")
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<UserResponse> deleteFromFavorites(@RequestParam Long recipeId, @RequestParam Long userId) {
        return new ResponseEntity<>(userService.deleteFromFavorites(recipeId, userId), HttpStatus.OK);
    }

    @PostMapping(path = "/login")
    @Operation(summary = "Login user")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User successfully logged", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))), @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))})
    ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(userService.login(loginRequest), HttpStatus.OK);
    }
}
