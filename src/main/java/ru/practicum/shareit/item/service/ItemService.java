package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.List;

public interface ItemService {

    ItemDto addItem(Item item, long userId);

    ItemDto updateItem(long itemId, long userId);

    ItemDto getItem(long itemId);

    List<ItemDto> getAllOwnerItems(long ownerId);

    Collection<ItemDto> getNecessaryItem(String text);
}
