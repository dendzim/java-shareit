package ru.practicum.shareit.user.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.validation.OnCreate;
import ru.practicum.shareit.validation.OnUpdate;

@Data
@Valid
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank(groups = OnCreate.class, message = "Имя пользователя не может быть пустым")
    private String name;

    @NotBlank(groups = OnCreate.class, message = "Почта не должна быть пустой")
    @Email(groups = {OnCreate.class, OnUpdate.class}, message = "Почта должна быть в нужном формате")
    private String email;
}
