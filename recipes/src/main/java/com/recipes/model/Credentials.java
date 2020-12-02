package com.recipes.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credentials {
    @Size(min = 2, max = 15, message = "Username should be less than 15 symbols")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Username should contain only characters")
    private String username;
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$"
            , message ="Password should contain at least one special symbol and one number")
    @Size(min = 8, message = "Password should be at least 8 symbols")
    private String password;

}
