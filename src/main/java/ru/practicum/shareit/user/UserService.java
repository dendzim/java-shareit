package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {

    UserDto addUser(User user);

    UserDto updateUser(Long id);

    Collection<UserDto> getAllUsers();

    UserDto getUser(Long id);

    void deleteUser(Long id);
}
