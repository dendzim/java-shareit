package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.InMemoryUserStorage;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final InMemoryUserStorage inMemoryUserStorage;

    @Override
    public UserDto addUser(User user) {
        return inMemoryUserStorage.addUser(user);
    }

    @Override
    public UserDto updateUser(Long id) {
        return inMemoryUserStorage.updateUser(id);
    }

    @Override
    public Collection<UserDto> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    @Override
    public UserDto getUser(Long id) {
        return inMemoryUserStorage.getUser(id);
    }

    @Override
    public void deleteUser(Long id) {
        inMemoryUserStorage.deleteUser(id);
    }
}
