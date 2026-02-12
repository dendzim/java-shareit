package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dao.InMemoryItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final InMemoryItemStorage inMemoryItemStorage;

    @Override
    public ItemDto addItem(Item item, long userId) {
        return inMemoryItemStorage.addItem(item, userId);
    }

    @Override
    public ItemDto updateItem(long itemId, long userId) {
        return inMemoryItemStorage.updateItem(itemId, userId);
    }

    @Override
    public ItemDto getItem(long itemId) {
        return inMemoryItemStorage.getItem(itemId);
    }

    @Override
    public List<ItemDto> getAllOwnerItems(long ownerId) {
        return inMemoryItemStorage.getAllOwnerItems(ownerId);
    }

    @Override
    public Collection<ItemDto> getNecessaryItem(String text) {
        return inMemoryItemStorage.getNecessaryItem(text);
    }
}
