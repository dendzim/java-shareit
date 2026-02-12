package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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
    public UserDto addUser(@Valid @RequestBody final User user) {
        return userServiceImpl.addUser(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody final Long userId) {
        return userServiceImpl.updateUser(userId);
    }

    @GetMapping
    public Collection<UserDto> getAllUsers() {
        return userServiceImpl.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable final Long id) {
        return userServiceImpl.getUser(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable final Long id) {
        log.info("Пользователь с id: {} удален", id);
        userServiceImpl.deleteUser(id);
    }
}
