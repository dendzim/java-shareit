package ru.practicum.shareit.client;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.dto.UserDto;

@Component
public class UserClient extends BaseClient {

    public UserClient() {
        super("/users");
    }

    public Object addUser(UserDto user) {
        return post("", user);
    }

    public Object updateUser(UserDto userDto) {
        return patch("", userDto);
    }

    public Object getAllUsers() {
        return get("");
    }

    public Object getUser(Long userId) {
        return get("/" + userId);
    }

    public void deleteUser(Long userId) {
        delete("/"+ userId);
    }
}
