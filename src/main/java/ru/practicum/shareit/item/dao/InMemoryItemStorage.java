package ru.practicum.shareit.item.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.*;

import static ru.practicum.shareit.item.dao.ItemMapper.toItem;
import static ru.practicum.shareit.item.dao.ItemMapper.toItemDto;
import static ru.practicum.shareit.user.dao.InMemoryUserStorage.users;

@Slf4j
@Repository
public class InMemoryItemStorage {
    public static final Map<Long, Item> items = new HashMap<>();

    private Long getNextId() {
        long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public ItemDto addItem(ItemDto itemDto, Long ownerId) {
        if (!users.containsKey(ownerId)) {
            log.warn("Владелец с указанным id " + ownerId + " не найден");
            throw new NotFoundException("Владелец с id " + ownerId + " не найден");
        }
        Item item = toItem(itemDto);
        item.setOwnerId(ownerId);
        item.setId(getNextId());
        items.put(item.getId(), item);
        return toItemDto(item);
    }

    public ItemDto updateItem(ItemDto itemDto, long userId) {
        if (!users.containsKey(userId)) {
            log.warn("Пользователь с указанным id " + userId + " не найден");
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        Item newItem = toItem(itemDto);
        newItem.setOwnerId(userId);
        Item item = items.get(newItem.getId());
        if (newItem.getName() != null) {
            item.setName(newItem.getName());
        }
        if (newItem.getDescription() != null) {
            item.setDescription(newItem.getDescription());
        }
        if (newItem.getAvailable() != null) {
            item.setAvailable(newItem.getAvailable());
        }

        return toItemDto(item);
    }

    public ItemDto getItem(long itemId) {
        if (!items.containsKey(itemId)) {
            log.warn("Предмет с указанным id " + itemId + " не найден");
            throw new NotFoundException("Предмет с id = " + itemId + " не найден");
        }
        return toItemDto(items.get(itemId));
    }

    public List<ItemDto> getAllOwnerItems(long ownerId) {
        List<ItemDto> ownerItems = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwnerId() == ownerId) {
                ownerItems.add(toItemDto(item));
            }
        }
        return ownerItems;
    }

    public List<ItemDto> getNecessaryItem(String text) {
        List<ItemDto> necessaryItems = new ArrayList<>();
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        text = text.toLowerCase(Locale.ROOT);
        for (Item item : items.values()) {
            if (item.getAvailable() && item.getName().toLowerCase(Locale.ROOT).contains(text)
                    || item.getDescription().toLowerCase(Locale.ROOT).contains(text)) {
                necessaryItems.add(toItemDto(item));
            }
        }
        return necessaryItems;
    }
}
