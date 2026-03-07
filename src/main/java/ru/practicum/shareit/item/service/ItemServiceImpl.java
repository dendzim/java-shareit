package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.practicum.shareit.item.dao.ItemMapper.toItem;
import static ru.practicum.shareit.item.dao.ItemMapper.toItemDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public ItemDto addItem(ItemDto itemDto, Long ownerId) {
        if (!userRepository.existsById(ownerId)) {
            log.warn("Владелец с указанным id " + ownerId + " не найден");
            throw new NotFoundException("Владелец с id " + ownerId + " не найден");
        }
        Item item = toItem(itemDto);
        item.setOwnerId(ownerId);
        return toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemDto itemDto, long userId) {
        if (!userRepository.existsById(userId)) {
            log.warn("Пользователь с указанным id " + userId + " не найден");
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        Item newItem = toItem(itemDto);
        newItem.setOwnerId(userId);
        return toItemDto(itemRepository.save(newItem));
    }

    @Override
    @Transactional
    public ItemDto getItem(long itemId) {
        Item item = toItemDto(itemRepository.findById(itemId));
        ItemDto itemDto = toItemDto(item);
        Collection<Comment> comments = commentRepository.findByItemId(itemId);

        return itemDto;
    }

    @Override
    @Transactional
    public List<ItemDto> getAllOwnerItems(long ownerId) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemRepository.findByOwnerId(ownerId)) {
            itemDtoList.add(toItemDto(item));
        }
        return itemDtoList;
    }

    @Override
    public List<ItemDto> getNecessaryItem(String text) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemRepository.searchByNameOrDescription(text)) {
            itemDtoList.add(toItemDto(item));
        }
        return itemDtoList;
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentText) {
        return null;
    }
}
