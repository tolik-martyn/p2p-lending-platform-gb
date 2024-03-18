package com.example.userservice.service;

import com.example.userservice.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    User getUserById(Long id);

    List<User> getAllUsers();

    void deleteUser(Long id);

    User updateUser(Long id, User userDetails);
}
