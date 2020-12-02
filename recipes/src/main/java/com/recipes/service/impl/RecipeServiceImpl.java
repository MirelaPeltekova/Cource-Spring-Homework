package com.recipes.service.impl;

import com.recipes.dao.RecipeRepository;
import com.recipes.exceptions.InvalidAuthenticationException;
import com.recipes.exceptions.NonExistingentityException;
import com.recipes.model.Recipe;
import com.recipes.model.User;
import com.recipes.service.RecipeService;
import com.recipes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {

    private RecipeRepository recipeRepository;

    private UserService userService;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository, UserService userService) {
        this.recipeRepository = recipeRepository;
        this.userService = userService;
    }

    @Override
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    public Recipe getRecipeById(String id) {

        return recipeRepository.findById(id).orElseThrow(() ->
                new NonExistingentityException(String.format("Recipe with ID %s does not exist.", id)));
    }

    private boolean isAdmin() {
        Collection<? extends GrantedAuthority> roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        boolean isAdmin = roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return isAdmin;
    }

    @Override
    public Recipe addRecipe(Recipe recipe) {
        String username = "administrator";
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            username = SecurityContextHolder.getContext().getAuthentication().getName();
        }
        User author = userService.getUserByUsername(username);
        recipe.setRecipeId(null);
        recipe.setUserId(author.getUserId());

        return recipeRepository.insert(recipe);
    }

    @Override
    public Recipe updateRecipe(Recipe recipe) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!(userService.getUserByUsername(username).getUserId().equals(recipe.getUserId()))
                && !isAdmin()) {
            throw new InvalidAuthenticationException("You are not allowed as you are not creator of the recipe");
        }

        getRecipeById(recipe.getRecipeId());
        recipe.setModified(LocalDateTime.now());
        return recipeRepository.save(recipe);
    }

    @Override
    public Recipe deleteRecipe(String id) {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new BadCredentialsException("You are not logged");
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!(userService.getUserByUsername(username).getUserId().equals(getRecipeById(id).getUserId()))
                && !isAdmin()) {
            throw new InvalidAuthenticationException("You are not allowed as you are not creator of the recipe");
        }

        Recipe removed = getRecipeById(id);
        recipeRepository.deleteById(id);
        return removed;
    }

    @Override
    public long getCount() {
        return recipeRepository.count();
    }
}
