package ru.practicum.shareit.item.dao;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;

@UtilityClass
public class ItemMapper {
    public static ItemDto mapToItem(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }
}
