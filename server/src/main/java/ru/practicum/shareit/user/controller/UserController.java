package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validation.OnCreate;
import ru.practicum.shareit.validation.OnUpdate;

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
    public User addUser(@Validated(OnCreate.class) @RequestBody final UserDto user) {
        log.info("Пользователь добавлен");
        return userServiceImpl.addUser(user);
    }

    @PatchMapping("/{userId}")
    public User updateUser(@PathVariable Long userId,
                              @Validated(OnUpdate.class) @RequestBody final UserDto userDto) {
        log.info("Пользователь обновлен");
        return userServiceImpl.updateUser(userId, userDto);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
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