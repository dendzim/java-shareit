package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void shouldGetUser() {
        User user = new User(1L, "name", "email@test.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUser(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("name");
        verify(userRepository).findById(1L);
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenGetUserWithInvalidId() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUser(999L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void shouldAddUser() {
        UserDto userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("email@test.com");

        User savedUser = new User(1L, "name", "email@test.com");
        when(userRepository.save(any())).thenReturn(savedUser);

        User result = userService.addUser(userDto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("name");
        verify(userRepository).save(any());
    }

    @Test
    public void shouldUpdateUser() {
        User currentUser = new User(1L, "old", "old@test.com");
        UserDto updateDto = new UserDto();
        updateDto.setName("new");
        updateDto.setEmail("new@test.com");

        when(userRepository.existsById(1L)).thenReturn(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(currentUser));
        when(userRepository.save(any())).thenReturn(currentUser);

        User result = userService.updateUser(1L, updateDto);

        verify(userRepository).existsById(1L);
        verify(userRepository).findById(1L);
        verify(userRepository).save(any());

        assertThat(result.getName()).isEqualTo("new");
        assertThat(result.getEmail()).isEqualTo("new@test.com");
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenUpdateNonExistentUser() {
        Long nonExistentId = 999L;
        UserDto userDto = new UserDto("Updated", "updated@test.com");

        when(userRepository.existsById(nonExistentId)).thenReturn(false);

        assertThatThrownBy(() -> userService.updateUser(999L, userDto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void shouldDeleteUser() {
        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }
}
