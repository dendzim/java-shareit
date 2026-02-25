package ru.practicum.shareit.request.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Valid
public class ItemRequest {
    @NotNull
    private Long id;
    private String description;
    private User requestor;
    private LocalDate created;
}
