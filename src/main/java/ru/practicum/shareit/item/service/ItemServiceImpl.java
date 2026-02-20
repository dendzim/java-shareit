package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.InMemoryItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final InMemoryItemStorage inMemoryItemStorage;

    @Override
    public ItemDto addItem(ItemDto itemDto, Long ownerId) {
        return inMemoryItemStorage.addItem(itemDto, ownerId);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long userId) {
        return inMemoryItemStorage.updateItem(itemDto, userId);
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
