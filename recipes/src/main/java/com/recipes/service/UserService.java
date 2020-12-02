package com.recipes.service;

import com.recipes.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(String id);
    User getUserByUsername(String username);
    User addUser(User User);
    User updateUser(User User);
    User deleteUser(String id);
    long getCount();
}
