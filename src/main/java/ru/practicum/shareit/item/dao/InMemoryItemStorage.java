package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;


@Slf4j
@Repository
@RequiredArgsConstructor
public class InMemoryItemStorage {
    private final Map<Long, Item> items = new HashMap<>();

    private Long getNextId() {
        long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public Item addItem(Item item) {
        item.setId(getNextId());
        items.put(item.getId(), item);
        return item;
    }

    public Item updateItem(Item newItem) {
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
        return item;
    }

    public Item getItem(long itemId) {
        if (!items.containsKey(itemId)) {
            log.warn("Предмет с указанным id " + itemId + " не найден");
            throw new NotFoundException("Предмет с id = " + itemId + " не найден");
        }
        return items.get(itemId);
    }

    public List<Item> getAllOwnerItems(long ownerId) {
        List<Item> ownerItems = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwnerId() == ownerId) {
                ownerItems.add(item);
            }
        }
        return ownerItems;
    }

    public List<Item> getNecessaryItem(String text) {
        List<Item> necessaryItems = new ArrayList<>();
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        text = text.toLowerCase(Locale.ROOT);
        for (Item item : items.values()) {
            if (item.getAvailable() && item.getName().toLowerCase(Locale.ROOT).contains(text)
                    || item.getDescription().toLowerCase(Locale.ROOT).contains(text)) {
                necessaryItems.add(item);
            }
        }
        return necessaryItems;
    }
}
