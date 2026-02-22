package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.InMemoryItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.item.dao.ItemMapper.toItem;
import static ru.practicum.shareit.item.dao.ItemMapper.toItemDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final InMemoryItemStorage inMemoryItemStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    @Override
    public ItemDto addItem(ItemDto itemDto, Long ownerId) {
        if (!inMemoryUserStorage.getUsers().containsKey(ownerId)) {
            log.warn("Владелец с указанным id " + ownerId + " не найден");
            throw new NotFoundException("Владелец с id " + ownerId + " не найден");
        }
        Item item = toItem(itemDto);
        item.setOwnerId(ownerId);
        return toItemDto(inMemoryItemStorage.addItem(item));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long userId) {
        if (!inMemoryUserStorage.getUsers().containsKey(userId)) {
            log.warn("Пользователь с указанным id " + userId + " не найден");
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        Item newItem = toItem(itemDto);
        newItem.setOwnerId(userId);
        return toItemDto(inMemoryItemStorage.updateItem(newItem));
    }

    @Override
    public ItemDto getItem(long itemId) {
        return toItemDto(inMemoryItemStorage.getItem(itemId));
    }

    @Override
    public List<ItemDto> getAllOwnerItems(long ownerId) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : inMemoryItemStorage.getAllOwnerItems(ownerId)) {
            itemDtoList.add(toItemDto(item));
        }
        return itemDtoList;
    }

    @Override
    public List<ItemDto> getNecessaryItem(String text) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : inMemoryItemStorage.getNecessaryItem(text)) {
            itemDtoList.add(toItemDto(item));
        }
        return itemDtoList;
    }
}
