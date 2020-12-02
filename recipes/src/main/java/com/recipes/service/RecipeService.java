package com.recipes.service;

import com.recipes.model.Recipe;

import java.util.List;

public interface RecipeService {
    List<Recipe> getAllRecipes();
    Recipe getRecipeById(String id);
    Recipe addRecipe(Recipe Recipe);
    Recipe updateRecipe(Recipe Recipe);
    Recipe deleteRecipe(String id);
    long getCount();
}
