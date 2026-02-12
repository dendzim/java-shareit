package ru.practicum.shareit.user.dao;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.practicum.shareit.user.UserMapper.mapToUser;

@Slf4j
@Component
public class InMemoryUserStorage {
    private static Map<Long, User> users = new HashMap<>();

    private Long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public UserDto addUser(User user) {
        for (User usr : users.values()) {
            if (usr.getEmail().equals(user.getEmail())) {
                throw new ValidationException("Нельзя добавить пользователя с одинаковыми почтами");
            }
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        return mapToUser(user);
    }

    public UserDto updateUser(Long userId) {
        if (!users.containsKey(userId)) {
            log.warn("Пользователь с указанным id не найден");
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        User user = users.get(userId);
        user.setEmail(user.getEmail());
        user.setName(user.getName());
        return mapToUser(user);
    }

    public List<UserDto> getAllUsers() {
        List<UserDto> dtoList = new ArrayList<>();
        for (User user : users.values()) {
            dtoList.add(mapToUser(user));
        }
        return dtoList;
    }

    public UserDto getUser(Long id) {
        User user = users.get(id);
        return mapToUser(user);
    }

    public void deleteUser(Long id) {
        users.remove(id);
    }
}
