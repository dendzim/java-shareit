package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;


/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @PostMapping
    public UserDto addUser(@Valid @RequestBody final UserDto user) {
        log.info("Пользователь добавлен");
        return userServiceImpl.addUser(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable("userId") final Long userId, @Valid @RequestBody final UserDto userDto) {
        userDto.setId(userId);
        log.info("Пользователь обновлен");
        return userServiceImpl.updateUser(userDto);
    }

    @GetMapping
    public Collection<UserDto> getAllUsers() {
        log.info("Список пользователей выведен");
        return userServiceImpl.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable("userId")  final Long userId) {
        log.info("Польователь с id " + userId + " выведен");
        return userServiceImpl.getUser(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") final Long userId) {
        log.info("Пользователь с id " + userId + " удален");
        userServiceImpl.deleteUser(userId);
    }
}
