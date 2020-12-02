package com.recipes.init;

import com.recipes.model.Gender;
import com.recipes.model.Recipe;
import com.recipes.model.Role;
import com.recipes.model.User;
import com.recipes.service.RecipeService;
import com.recipes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private UserService userService;
    private RecipeService recipeService;

    private static final List<User> SAMPLE_USERS = List.of(
            new User("Mirela", "administrator", "Admin123@",
                    Role.ADMIN, Gender.FEMALE, "https://www.istockphoto.com/vector/default-female-avatar-profile-picture-icon-grey-woman-photo-placeholder-gm653035772-118646091"),
            new User("Ivan", "ivanAtanasov", "Ivan1234@",
                    Role.USER, Gender.MALE, "https://www.shutterstock.com/image-vector/default-avatar-profile-icon-grey-photo-518740741")
    );
    private static final List<Recipe> SAMPLE_RECIPES = List.of(
            new Recipe("Brownies", "These brownies are magnificent!!! I mean \"melt in your mouth\"", (short) 50, List.of("chocolate", "butter", "sugar"), "https://cdn.iconscout.com/icon/premium/png-512-thumb/public-domain-user-618551.p",
                    "Preheat oven to 350 degrees F (175 degrees C). Grease and flour an 8-inch square pan.\n" +
                            "In a large saucepan, melt 1/2 cup butter. Remove from heat, and stir in sugar, eggs, and 1 teaspoon vanilla. Beat in 1/3 cup cocoa, 1/2 cup flour, salt, and baking powder. Spread batter into prepared pan.\n" +
                            "Bake in preheated oven for 25 to 30 minutes. Do not overcook.\n" +
                            "To Make Frosting: Combine 3 tablespoons softened butter, 3 tablespoons cocoa, honey, 1 teaspoon vanilla extract, and 1 cup confectioners' sugar. Stir until smooth. Frost brownies while they are still warm.",
                    List.of("chocolate", "dessert")),
            new Recipe("Skillet Lasagna", "This no-bake skillet lasagna is made right on your stovetop and is a fast and easy alternative to store-bought hamburger mixes!", (short) 60, List.of("beef", "tomatoes", "spaghetti "),
                    "https://www.pamperedchef.com/iceberg/com/recipe/77522-lg.jpg",
                    "Heat a large skillet over medium-high heat. Cook and stir beef in the hot skillet until browned and crumbly, 5 to 7 minutes. Drain and discard grease. Add spaghetti sauce, tomatoes, onion, garlic, basil, oregano, salt, and pepper. Cook over low heat until sauce is hot, about 15 minutes.\n" +
                            "Meanwhile, fill a large pot with lightly salted water and bring to a rolling boil. Cook mafalda noodles at a boil until tender yet firm to the bite, about 8 minutes. Drain.\n" +
                            "Add cooked and drained noodles to the sauce and stir until completely coated. Sprinkle mozzarella cheese on top.\n" +
                            "Set an oven rack about 6 inches from the heat source and preheat the oven's broiler.\n" +
                            "Place skillet under the hot broil and cook until cheese is golden and bubbly, 3 to 5 minutes.",
                    List.of("meal", "fast dinner"))
    );

    @Autowired
    public DataInitializer(UserService userService, RecipeService recipeService) {
        this.userService = userService;
        this.recipeService = recipeService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userService.getCount() == 0) {
            SAMPLE_USERS.forEach(userService::addUser);
        }

        if (recipeService.getCount() == 0) {
            SAMPLE_RECIPES.forEach(recipe -> {
                recipe.setUserId(userService.getUserByUsername("administrator").getUserId());
                recipeService.addRecipe(recipe);
            });
        }
    }
}
