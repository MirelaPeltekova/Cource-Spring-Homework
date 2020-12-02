package com.recipes.web;

import com.recipes.dao.dto.UserLoggingDto;
import com.recipes.exceptions.InvalidEntityDataException;
import com.recipes.model.*;
import com.recipes.service.UserService;

import com.recipes.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

import java.net.URI;

import static com.recipes.utils.ErrorHadlingUtils.getViolationsAsStringLists;


@RestController
@Slf4j
public class LoginController {

    private UserService userService;
    private JwtUtils jwtUtils;
    private AuthenticationManager authenticationManager;

    @Autowired
    public LoginController(UserService userService, JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/api/login")
    public JwtResponse login(@Valid @RequestBody Credentials credentials, Errors errors) {
        if (errors.hasErrors()) {
            throw new InvalidEntityDataException("Invalid username or password");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getUsername(),
                credentials.getPassword()));

        final User user = userService.getUserByUsername(credentials.getUsername());
        final String token = jwtUtils.generateToken(user);
        log.info("Login sucessfull for {}: {}", user.getUsername(), token);
        return new JwtResponse(new UserLoggingDto(user.getName(), user.getUsername(), user.getGender(),
                user.getImageURL(), user.getPersonalInfo()), token);
    }

    @PostMapping("/api/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user, Errors errors) {
        if (errors.hasErrors()) {
            throw new InvalidEntityDataException("Invalid user data", getViolationsAsStringLists(errors));
        }

        User createdUser= userService.addUser(user);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("api/users/{userID}")
                .buildAndExpand(createdUser.getUserId()).toUri();
        return ResponseEntity.created(location).body(createdUser);
    }

}
