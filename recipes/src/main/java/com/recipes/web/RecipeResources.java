package com.recipes.web;

import com.recipes.exceptions.InvalidEntityDataException;
import com.recipes.model.Recipe;
import com.recipes.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

import static com.recipes.utils.ErrorHadlingUtils.getViolationsAsStringLists;

@RestController
@RequestMapping("/api/recipes")
public class RecipeResources {

    private RecipeService recipeService;

    @Autowired
    public RecipeResources(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();

    }

    @GetMapping("/{recipeId}")
    public Recipe getRecipeById(@PathVariable("recipeId") String id) {
        return recipeService.getRecipeById(id);
    }

    @PostMapping
    public ResponseEntity<Recipe> createRecipe(@Valid @RequestBody Recipe recipe, Errors errors) {
        if (errors.hasErrors()) {
            throw new InvalidEntityDataException("Invalid recipe data", getViolationsAsStringLists(errors));
        }
        Recipe created = recipeService.addRecipe(recipe);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{recipeId}")
                        .buildAndExpand(created.getRecipeId()).toUri()
        ).body(created);
    }

    @DeleteMapping("{recipeId}")
    public Recipe deleteRecipe(@PathVariable("recipeId") String id) {
        Recipe removed = getRecipeById(id);
        recipeService.deleteRecipe(id);
        return removed;
    }

    @PutMapping("{recipeId}")
    public Recipe updateRecipe(@Valid @RequestBody Recipe recipe, @PathVariable("recipeId") String id, Errors errors) {

        if (errors.hasErrors()) {
            throw new InvalidEntityDataException("Invalid recipe data", getViolationsAsStringLists(errors));
        }

        if (!id.equals(recipe.getRecipeId())) {
            throw new InvalidEntityDataException(
                    String.format("Recipe ID:%s differs from body id: %s ", id, recipe.getRecipeId())
            );
        }
        return recipeService.updateRecipe(recipe);

    }


}
