package ru.practicum.shareit.user.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
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
    private final UserRepository userRepository;


    @Override
    @Transactional
    public UserDto addUser(UserDto userDto) {
        User user = toUser(userDto);
        return toUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto) {
        if (!userRepository.existsById(userDto.getId())) {
            log.warn("Пользователь с указанным id не найден");
            throw new NotFoundException("Пользователь с id = " + userDto.getId() + " не найден");
        }

        User newUser = getUser(userDto.getId());
        if (userDto.getName() != null) {
            newUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            newUser.setEmail(userDto.getEmail());
        }
        return toUserDto(userRepository.save(newUser));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> dtoList = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            dtoList.add(toUserDto(user));
        }
        return dtoList;
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Пользователь с id = " + id + " не найден"));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
