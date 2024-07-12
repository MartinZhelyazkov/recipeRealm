package com.example.recipeRealm.service.impl;

import com.example.recipeRealm.advice.exceptions.RecordNotFoundException;
import com.example.recipeRealm.converter.RecipeConverter;
import com.example.recipeRealm.dto.RecipeRequest;
import com.example.recipeRealm.dto.RecipeResponse;
import com.example.recipeRealm.model.Comment;
import com.example.recipeRealm.model.Recipe;
import com.example.recipeRealm.model.User;
import com.example.recipeRealm.repository.RecipeRepository;
import com.example.recipeRealm.repository.UserRepository;
import com.example.recipeRealm.service.RecipeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;

    private final RecipeConverter recipeConverter;

    private final UserRepository userRepository;

    private final CurrentUserService currentUserService;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeConverter recipeConverter, UserRepository userRepository, CurrentUserService currentUserService) {
        this.recipeRepository = recipeRepository;
        this.recipeConverter = recipeConverter;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public RecipeResponse addRecipe(RecipeRequest recipeRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("User with id %s not found", userId)));
        Recipe recipe = recipeConverter.toRecipe(recipeRequest);
        recipe.setAuthor(user);
        Recipe saveRecipe = recipeRepository.save(recipe);
        boolean canAddRecipe = currentUserService.isCurrentUserMatch(recipe.getAuthor());
        canAddRecipe |= currentUserService.isCurrentUserARole("ROLE_ADMIN");
        if (!canAddRecipe) {
            throw new RecordNotFoundException("This user can't add recipe");
        }
        RecipeResponse recipeResponse = new RecipeResponse();
        BeanUtils.copyProperties(saveRecipe, recipeResponse);
        return recipeResponse;
    }

    @Override
    public RecipeResponse findRecipe(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Recipe with id %s not found", recipeId)));
        RecipeResponse recipeResponse = new RecipeResponse();
        BeanUtils.copyProperties(recipe, recipeResponse);
        return recipeResponse;
    }

    @Override
    public RecipeResponse updateRecipe(RecipeRequest recipeRequest, Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Recipe with id %s not found", recipeId)));
        boolean canUpdateRecipe = currentUserService.isCurrentUserMatch(recipe.getAuthor());
        canUpdateRecipe |= currentUserService.isCurrentUserARole("ROLE_ADMIN");
        if (!canUpdateRecipe) {
            throw new RecordNotFoundException("This user can't update this recipe");
        }
        recipe.setDescription(recipeRequest.getDescription());
        recipe.setIngredients(recipeRequest.getIngredients());
        recipe.setInstructions(recipeRequest.getInstructions());
        recipe.setCategory(recipeRequest.getCategory());
        recipe.setTitle(recipeRequest.getTitle());
        Recipe saveRecipe = recipeRepository.save(recipe);
        RecipeResponse recipeResponse = new RecipeResponse();
        BeanUtils.copyProperties(saveRecipe, recipeResponse);
        return recipeResponse;
    }

    @Override
    public void delRecipe(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Recipe with id %s not found", recipeId)));
        boolean canDeleteRecipe = currentUserService.isCurrentUserMatch(recipe.getAuthor());
        canDeleteRecipe |= currentUserService.isCurrentUserARole("ROLE_ADMIN");
        if (!canDeleteRecipe) {
            throw new RecordNotFoundException("This user can't delete this recipe");
        }
        recipeRepository.deleteById(recipeId);
    }

    @Override
    public Set<RecipeResponse> findAllRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();
        Set<RecipeResponse> recipeResponsesSet = new HashSet<>();
        for (Recipe recipe : recipes) {
            RecipeResponse recipeResponse = new RecipeResponse();
            BeanUtils.copyProperties(recipe, recipeResponse);
            recipeResponsesSet.add(recipeResponse);
        }
        return recipeResponsesSet;
    }

    @Override
    public RecipeResponse likeRecipe(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Recipe with id %s not found", recipeId)));
        recipe.setLikes(recipe.getLikes() + 1);
        recipeRepository.save(recipe);
        RecipeResponse recipeResponse = new RecipeResponse();
        BeanUtils.copyProperties(recipe, recipeResponse);
        return recipeResponse;
    }

    @Override
    public RecipeResponse dislikeRecipe(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(
                () -> new RecordNotFoundException(String.format("Recipe with id %s not found", recipeId)));
        recipe.setDislikes(recipe.getDislikes() + 1);
        recipeRepository.save(recipe);
        RecipeResponse recipeResponse = new RecipeResponse();
        BeanUtils.copyProperties(recipe, recipeResponse);
        return recipeResponse;
    }

    @Override
    public Set<Comment> getAllCommentsForRecipe(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Recipe with id %s not found", recipeId)));
        return recipe.getComments();
    }

    @Override
    public Set<Recipe> findRecipesByTitleAndCategory(String title, String category) {
        return recipeRepository.findRecipesByTitleAndCategory(title, category);
    }

    @Override
    public Set<RecipeResponse> getAllFavoriteRecipes(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("User with id %s not found", userId)));
        Set<Recipe> favoriteRecipes = user.getFavoriteRecipes();
        Set<RecipeResponse> recipeResponsesSet = new HashSet<>();
        for (Recipe recipe : favoriteRecipes) {
            RecipeResponse recipeResponse = new RecipeResponse();
            BeanUtils.copyProperties(recipe, recipeResponse);
            recipeResponsesSet.add(recipeResponse);
        }
        return recipeResponsesSet;
    }
}

