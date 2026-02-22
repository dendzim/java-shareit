package ru.practicum.shareit.user.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dao.InMemoryUserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.user.dao.UserMapper.toUser;
import static ru.practicum.shareit.user.dao.UserMapper.toUserDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = toUser(userDto);
        if (user.getEmail() == null) {
            log.warn("Некорректная почта");
            throw new BadRequestException("некорректный тип почты");
        }
        for (User usr : inMemoryUserStorage.getUsers().values()) {
            if (usr.getEmail().equals(user.getEmail())) {
                log.warn("такая почта уже используется");
                throw new ValidationException("Нельзя добавить существующую почту");
            }
        }
        return toUserDto(inMemoryUserStorage.addUser((user)));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        for (User usr : inMemoryUserStorage.getUsers().values()) {
            if (usr.getEmail().equals(userDto.getEmail())) {
                log.warn("такая почта уже используется");
                throw new ValidationException("Нельзя добавить существующую почту");
            }
        }
        if (!inMemoryUserStorage.getUsers().containsKey(userDto.getId())) {
            log.warn("Пользователь с указанным id не найден");
            throw new NotFoundException("Пользователь с id = " + userDto.getId() + " не найден");
        }
        User newUser = toUser(userDto);
        return toUserDto(inMemoryUserStorage.updateUser(newUser));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> dtoList = new ArrayList<>();
        for (User user : inMemoryUserStorage.getAllUsers()) {
            dtoList.add(toUserDto(user));
        }
        return dtoList;
    }

    @Override
    public UserDto getUser(Long id) {
        return toUserDto(inMemoryUserStorage.getUser(id));
    }

    @Override
    public void deleteUser(Long id) {
        inMemoryUserStorage.deleteUser(id);
    }
}
