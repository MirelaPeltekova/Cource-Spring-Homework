package com.recipes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document("recipes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {

    @Id
    @Pattern(regexp = "[A-Za-z0-9]{24}")
    private String recipeId;
    @Pattern(regexp = "[A-Za-z0-9]{24}")
    private String userId;
    @NonNull
    @Size(max=80, message = "Name should be up to 80 symbols")
    private String name;
    @Size(max = 256, message = "Short description should be up to 256 symbols")
    private String shortDescription;
    private short cookingMinutes;
    private List<String> products= new ArrayList<>();
    @NonNull
    @URL
    private String imageURL;
    @Size(max = 2048, message = "Detailed description should be up to 256 symbols")
    private String detailedDescription;
    private List<String> tags= new ArrayList<>();
    @PastOrPresent
    private LocalDateTime shared = LocalDateTime.now();
    @PastOrPresent
    private LocalDateTime modified = LocalDateTime.now();

    public Recipe(@NonNull @Size(max = 80, message = "Name should be up to 80 symbols") String name,
                  @Size(max = 256, message = "Short description should be up to 256 symbols") String shortDescription,
                  short cookingMinutes,
                  List<String> products,
                  @NonNull @URL String imageURL,
                  @Size(max = 2048, message = "Detailed description should be up to 256 symbols") String detailedDescription,
                  List<String> tags) {
        this.name = name;
        this.shortDescription = shortDescription;
        this.cookingMinutes = cookingMinutes;
        this.products = products;
        this.imageURL = imageURL;
        this.detailedDescription = detailedDescription;
        this.tags = tags;
    }

}
