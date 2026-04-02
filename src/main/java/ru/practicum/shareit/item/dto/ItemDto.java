package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.OnCreate;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;
    @NotBlank(groups = OnCreate.class, message = "Название предмета должно быть заполнено")
    private String name;
    @NotBlank(groups = OnCreate.class, message = "Описание предмета должно быть заполнено")
    private String description;
    @NotNull(groups = OnCreate.class, message = "Статус должен быть заполнен")
    private Boolean available;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
    private Collection<CommentDto> comments;
}
