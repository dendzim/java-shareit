package ru.practicum.shareit.user.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDtoTest {

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
    }

    @Test
    void testNoArgsConstructor() {
        assertNotNull(userDto);
        assertNull(userDto.getEmail());
        assertNull(userDto.getName());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        String name = "Test";
        String email = "test@example.com";


        UserDto dto = new UserDto(name, email);

        assertEquals(name, dto.getName());
        assertEquals(email, dto.getEmail());
    }
}
