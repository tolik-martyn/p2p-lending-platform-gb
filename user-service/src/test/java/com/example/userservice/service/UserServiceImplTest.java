package com.example.userservice.service;

import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DisplayName("User Service Tests")
public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("Create User Test")
    public void createUser() {
        User user = new User();

        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertEquals(user, createdUser);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    @DisplayName("Get User By Id Test")
    public void getUserById() {
        User user = new User();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1L);

        assertEquals(user, foundUser);
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    @DisplayName("Get User By Id Not Found Test")
    public void getUserByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    @DisplayName("Get All Users Test")
    public void getAllUsers() {
        List<User> users = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(users);

        List<User> foundUsers = userService.getAllUsers();

        assertEquals(users, foundUsers);
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Delete User Test")
    public void deleteUser() {
        userService.deleteUser(1L);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Update User Test")
    public void updateUser() {
        User user = new User();

        User userDetails = new User();
        userDetails.setFirstName("Николас");
        userDetails.setLastName("Кейдж");
        userDetails.setEmail("Николас@Кейдж.ru");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = userService.updateUser(1L, userDetails);

        assertEquals(userDetails.getFirstName(), updatedUser.getFirstName());
        assertEquals(userDetails.getLastName(), updatedUser.getLastName());
        assertEquals(userDetails.getEmail(), updatedUser.getEmail());
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }
}
