package com.example.recipeRealm.repository;

import com.example.recipeRealm.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query(value = "SELECT * FROM recipes r WHERE r.title LIKE %:title% AND r.category LIKE %:category%", nativeQuery = true)
    Set<Recipe> findRecipesByTitleAndCategory(String title, String category);
}
