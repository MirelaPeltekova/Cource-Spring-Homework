package com.recipes.dao.dto;
import com.recipes.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLoggingDto {
    private String name;
    private String username;
    private Gender gender;
    private String imageURL;
    private String personalInfo;

}
