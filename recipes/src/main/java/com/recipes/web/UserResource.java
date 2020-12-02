package com.recipes.web;

import com.recipes.exceptions.InvalidEntityDataException;
import com.recipes.model.User;
import com.recipes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

import static com.recipes.utils.ErrorHadlingUtils.getViolationsAsStringLists;

@RestController
@RequestMapping("/api/users")
public class UserResource {

    private UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();

    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable("userId") String id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user, Errors errors) {
        if (errors.hasErrors()) {
            throw new InvalidEntityDataException("Invalid user data", getViolationsAsStringLists(errors));
        }

        User created = userService.addUser(user);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{userId}")
                        .buildAndExpand(created.getUserId()).toUri()
        ).body(created);
    }

    @DeleteMapping("{userId}")
    public User deleteUser(@PathVariable("userId") String id) {
        User removed = getUserById(id);
        userService.deleteUser(id);
        return removed;
    }


    @PutMapping("{userId}")
    public User updateUser(@Valid @RequestBody User User, @PathVariable("userId") String id, Errors errors) {
        if (errors.hasErrors()) {
            throw new InvalidEntityDataException("Invalid user data", getViolationsAsStringLists(errors));
        }

        if (!id.equals(User.getUserId())) {
            throw new InvalidEntityDataException(
                    String.format("User ID:%s differs from body id: %s ", id, User.getUserId())
            );
        }
        return userService.updateUser(User);

    }

}
