package ru.practicum.shareit.user.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Valid
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    @Email(message = "Email should be valid")
    private String email;
}
