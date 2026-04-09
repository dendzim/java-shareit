package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.Collection;
import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, Long userId);

    ItemDto updateItem(ItemDto itemDto, Long itemId,  long userId);

    ItemDto getItem(long itemId);

    List<ItemDto> getAllOwnerItems(long ownerId);

    Collection<ItemDto> getNecessaryItem(String text);

    CommentDto addComment(Long itemId, Long userId, Comment comment);
}
