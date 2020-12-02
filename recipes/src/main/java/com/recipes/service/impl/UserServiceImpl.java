package com.recipes.service.impl;

import com.recipes.dao.UserRepository;
import com.recipes.exceptions.InvalidEntityDataException;
import com.recipes.exceptions.NonExistingentityException;
import com.recipes.model.Gender;
import com.recipes.model.Role;
import com.recipes.model.User;
import com.recipes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepo;

    @Autowired
    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserById(String id) {
        return userRepo.findById(id).orElseThrow(() ->
                new NonExistingentityException(String.format("User with ID %s does not exist.", id)));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(
                () -> new InvalidEntityDataException("Invalid username or password"));
    }

    @Override
    public User addUser(User user) {
        user.setUserId(null);
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        user.setPassword(encoder.encode((user.getPassword())));

        if (userRepo.existsByName(user.getName())) {
            throw new InvalidEntityDataException(String.format("Name %s already exists. Please choose another name.", user.getName()));
        }

        //setting default images if not specified
        if (user.getImageURL() == null) {
            if (user.getGender() == Gender.FEMALE) {

                user.setImageURL("http://cldbm.qc.ca/wp-content/uploads/2020/02/person-gray-photo-placeholder-girl-material-design-vector-23804677.jpg");
            } else {
                user.setImageURL("https://www.dreamstime.com/stock-illustration-default-placeholder-profile-icon-avatar-gray-man-image90197993");
            }
        }

        //We have initial data so some of the users might already have role
        if(user.getRole()==null){
            user.setRole(Role.USER);
        }
        return userRepo.insert(user);
    }

    @Override
    public User updateUser(User user) {
        getUserById(user.getUserId());
        user.setModified(LocalDateTime.now());
        return userRepo.save(user);
    }

    @Override
    public User deleteUser(String id) {
        User deleted = getUserById(id);
        userRepo.deleteById(id);
        return deleted;
    }

    @Override
    public long getCount() {
        return userRepo.count();
    }
}
