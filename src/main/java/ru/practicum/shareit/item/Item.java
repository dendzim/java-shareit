package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Valid
@AllArgsConstructor
public class Item {
    @NotNull
    private Long id;
    private String name;
    private String description;
    private ItemStatus itemStatus;
    private Long owner;
    private ItemRequest request;

    public boolean isAvailable() {
        return itemStatus == ItemStatus.AVAILABLE;
    }

    public ItemRequest getRequest() {
        return null;
    }
}
