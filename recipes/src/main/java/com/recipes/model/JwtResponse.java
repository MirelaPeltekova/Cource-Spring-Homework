package com.recipes.model;

import com.recipes.dao.dto.UserLoggingDto;
import lombok.Data;

@Data
public class JwtResponse {
    //Using dto which is not retrieving the password but only specified fields,
    private final UserLoggingDto userLoggingDto;
    private final String token;

}
