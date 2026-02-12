package ru.practicum.shareit.item.dao;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.*;

import static ru.practicum.shareit.item.dao.ItemMapper.mapToItem;

@Slf4j
@Repository
public class InMemoryItemStorage {
    private static Map<Long, Item> items = new HashMap<>();

    private Long getNextId() {
        long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public ItemDto addItem(Item item, long userId) {
        item.setId(getNextId());
        item.setOwner(userId);
        items.put(item.getId(), item);
        return mapToItem(item);
    }

    public ItemDto updateItem(long itemId, long userId) {
        if (!items.containsKey(itemId)) {
            log.warn("Пользователь с указанным id не найден");
            throw new NotFoundException("Пользователь с id = " + itemId + " не найден");
        }
        Item item = items.get(itemId);
        if (item.getOwner() != userId) {
            log.warn("Только владелец может редактировать предмет");
            return null;
        }
        item.setName(items.get(itemId).getName());
        item.setDescription(items.get(itemId).getDescription());
        return mapToItem(item);
    }

    public ItemDto getItem(long itemId) {
        if (!items.containsKey(itemId)) {
            log.warn("Пользователь с указанным id не найден");
            throw new NotFoundException("Пользователь с id = " + itemId + " не найден");
        }
        return mapToItem(items.get(itemId));
    }

    public List<ItemDto> getAllOwnerItems(long ownerId) {
        List<ItemDto> ownerItems = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwner() == ownerId) {
                ownerItems.add(mapToItem(item));
            }
        }
        return ownerItems;
    }

    public List<ItemDto> getNecessaryItem(String text) {
        List<ItemDto> necessaryItems = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.isAvailable() && item.getDescription().contains(text)) {
                necessaryItems.add(mapToItem(item));
            }
        }
        return necessaryItems;
    }
}
