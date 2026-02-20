package ru.practicum.shareit.user.dao;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.practicum.shareit.user.dao.UserMapper.*;

@Slf4j
@Component
public class InMemoryUserStorage {
    public static final Map<Long, User> users = new HashMap<>();

    private Long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public UserDto addUser(UserDto user) {
        if (user.getEmail() == null) {
            log.warn("Некорректная почта");
            throw new BadRequestException("некорректный тип почты");
        }
        for (User usr : users.values()) {
            if (usr.getEmail().equals(user.getEmail())) {
                log.warn("такая почта уже используется");
                throw new ValidationException("Нельзя добавить существующую почту");
            }
        }
        user.setId(getNextId());
        users.put(user.getId(), toUser(user));
        return user;
    }

    public UserDto updateUser(UserDto userDto) {
        for (User usr : users.values()) {
            if (usr.getEmail().equals(userDto.getEmail())) {
                log.warn("такая почта уже используется");
                throw new ValidationException("Нельзя добавить существующую почту");
            }
        }
        if (!users.containsKey(userDto.getId())) {
            log.warn("Пользователь с указанным id не найден");
            throw new NotFoundException("Пользователь с id = " + userDto.getId() + " не найден");
        }
        User newUser = toUser(userDto);
        User user = users.get(newUser.getId());
        if (newUser.getName() != null) {
            user.setName(newUser.getName());
        }
        if (newUser.getEmail() != null) {
            user.setEmail(newUser.getEmail());
        }
        return toUserDto(user);
    }

    public List<UserDto> getAllUsers() {
        List<UserDto> dtoList = new ArrayList<>();
        for (User user : users.values()) {
            dtoList.add(toUserDto(user));
        }
        return dtoList;
    }

    public UserDto getUser(Long id) {
        User user = users.get(id);
        return toUserDto(user);
    }

    public void deleteUser(Long id) {
        users.remove(id);
    }
}
