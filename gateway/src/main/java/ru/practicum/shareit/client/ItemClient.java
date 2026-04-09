package ru.practicum.shareit.client;

import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.dto.CommentDto;
import ru.practicum.shareit.dto.ItemDto;

import java.util.Collection;

public class ItemClient extends BaseClient {
    public ItemClient(RestTemplate rest) {
        super(rest);
    }

    public Object addItem(ItemDto itemDto, Long ownerId) {
        return null;
    }

    public ItemDto updateItem(ItemDto itemDto, long itemId, long ownerId) {
        return null;
    }

    public ItemDto getItem(long itemId) {
        return null;
    }

    public Collection<ItemDto> getAllOwnerItems(long ownerId) {
        return null;
    }

    public Collection<ItemDto> getNecessaryItem(String text) {
        return null;
    }

    public CommentDto addComment(long ownerId, long itemId, CommentDto comment) {
        return null;
    }
}
