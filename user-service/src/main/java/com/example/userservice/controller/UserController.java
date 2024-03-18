package com.example.userservice.controller;

import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Value("${gateway.url}")
    private String gatewayUrl;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user-list";
    }

    @GetMapping("/{userId}")
    public String getUserById(@PathVariable("userId") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "user-details";
    }

    @GetMapping("/add")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        return "add-user";
    }

    @PostMapping("/add")
    public String createUser(@ModelAttribute User user) {
        userService.createUser(user);
        return "redirect:" + gatewayUrl + "/users";
    }

    @GetMapping("/{userId}/delete")
    public String showDeleteUserForm(@PathVariable("userId") Long id, Model model) {
        // Проверка на наличие пользователя с указанным id
        userService.getUserById(id);
        model.addAttribute("userId", id);
        return "delete-user";
    }

    @PostMapping("/{userId}/delete")
    public String deleteUser(@PathVariable("userId") Long id) {
        userService.deleteUser(id);
        return "redirect:" + gatewayUrl + "/users";
    }

    @GetMapping("/{userId}/update")
    public String showUpdateUserForm(@PathVariable("userId") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "update-user";
    }

    @PostMapping("/{userId}/update")
    public String updateUser(@PathVariable("userId") Long id, @ModelAttribute User user) {
        userService.updateUser(id, user);
        return "redirect:" + gatewayUrl + "/users";
    }
}
