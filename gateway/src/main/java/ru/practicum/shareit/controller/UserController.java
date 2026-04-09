package ru.practicum.shareit.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.OnCreate;
import ru.practicum.shareit.OnUpdate;
import ru.practicum.shareit.client.UserClient;
import ru.practicum.shareit.dto.UserDto;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public Object addUser(@Validated(OnCreate.class) @RequestBody final UserDto user) {
        log.info("Пользователь добавлен");
        return userClient.addUser(user);
    }

    @PatchMapping("/{userId}")
    public Object updateUser(@Positive @PathVariable ("userId") final Long userId,
                              @Validated(OnUpdate.class) @RequestBody final UserDto userDto) {
        userDto.setId(userId);
        log.info("Пользователь обновлен");
        return userClient.updateUser(userDto);
    }

    @GetMapping
    public Object getAllUsers() {
        log.info("Список пользователей выведен");
        return userClient.getAllUsers();
    }

    @GetMapping("/{userId}")
    public Object getUser(@Positive @PathVariable("userId")  final Long userId) {
        log.info("Польователь с id " + userId + " выведен");
        return userClient.getUser(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@Positive @PathVariable("userId") final Long userId) {
        log.info("Пользователь с id " + userId + " удален");
        userClient.deleteUser(userId);
    }
}