package ru.practicum.shareit.client;

import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.dto.UserDto;

import java.util.Collection;

public class UserClient extends BaseClient {
    public UserClient(RestTemplate rest) {
        super(rest);
    }

    public Object addUser(UserDto user) {
        return null;
    }

    public Object updateUser(UserDto userDto) {
        return null;
    }

    public Collection<Object> getAllUsers() {
        return null;
    }

    public Object getUser(Long userId) {
        return null;
    }

    public void deleteUser(Long userId) {
    }
}
