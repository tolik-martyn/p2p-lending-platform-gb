package com.example.userservice.controller;

import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@Tag(name = "User Controller", description = "Endpoints for managing users")
public class UserController {
    private final UserService userService;

    @Value("${gateway.url}")
    private String gatewayUrl;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Returns a list of all users.")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user-list";
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by ID", description = "Returns details of a user based on the provided ID.")
    public String getUserById(@PathVariable("userId") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "user-details";
    }

    @GetMapping("/add")
    @Operation(summary = "Show form to create user", description = "Displays a form for creating a new user.")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        return "add-user";
    }

    @PostMapping("/add")
    @Operation(summary = "Create user", description = "Creates a new user with the provided details.")
    public String createUser(@ModelAttribute User user) {
        userService.createUser(user);
        return "redirect:" + gatewayUrl + "/users";
    }

    @GetMapping("/{userId}/delete")
    @Operation(summary = "Show form to delete user", description = "Displays a confirmation form for deleting a user.")
    public String showDeleteUserForm(@PathVariable("userId") Long id, Model model) {
        // Проверка на наличие пользователя с указанным id
        userService.getUserById(id);
        model.addAttribute("userId", id);
        return "delete-user";
    }

    @PostMapping("/{userId}/delete")
    @Operation(summary = "Delete user", description = "Deletes the user with the provided ID.")
    public String deleteUser(@PathVariable("userId") Long id) {
        userService.deleteUser(id);
        return "redirect:" + gatewayUrl + "/users";
    }

    @GetMapping("/{userId}/update")
    @Operation(summary = "Show form to update user", description = "Displays a form for updating user details.")
    public String showUpdateUserForm(@PathVariable("userId") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "update-user";
    }

    @PostMapping("/{userId}/update")
    @Operation(summary = "Update user", description = "Updates the details of the user with the provided ID.")
    public String updateUser(@PathVariable("userId") Long id, @ModelAttribute User user) {
        userService.updateUser(id, user);
        return "redirect:" + gatewayUrl + "/users";
    }
}
