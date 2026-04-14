package ru.practicum.shareit.user.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserDto> jacksonTester;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto("name", "name@example.com");
    }

    @Test
    void shouldSerializeUserDto() throws IOException {
        JsonContent<UserDto> json = jacksonTester.write(userDto);

        assertThat(json).hasJsonPathStringValue("$.name");
        assertThat(json).hasJsonPathStringValue("$.email");
        assertThat(json).extractingJsonPathStringValue("$.name")
                .isEqualTo("name");
        assertThat(json).extractingJsonPathStringValue("$.email")
                .isEqualTo("name@example.com");
    }

    @Test
    void shouldDeserializeUserDto() throws IOException {
        String json = "{\"name\":\"name\",\"email\":\"name@example.com\"}";

        UserDto deserializedUser = jacksonTester.parseObject(json);

        assertThat(deserializedUser.getName()).isEqualTo("name");
        assertThat(deserializedUser.getEmail()).isEqualTo("name@example.com");
    }

    @Test
    void shouldDeserializeUserDtoWithNullFields() throws IOException {
        String json = "{\"name\":null,\"email\":null}";

        UserDto deserializedUser = jacksonTester.parseObject(json);

        assertThat(deserializedUser.getName()).isNull();
        assertThat(deserializedUser.getEmail()).isNull();
    }
}