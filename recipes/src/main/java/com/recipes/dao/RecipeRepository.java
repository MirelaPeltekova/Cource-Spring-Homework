package com.recipes.dao;

import com.recipes.model.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends MongoRepository<Recipe,String> {
    String findUserIdByRecipeId(String recipeId);
}
