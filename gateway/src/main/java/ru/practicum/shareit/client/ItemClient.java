package ru.practicum.shareit.client;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.dto.CommentDto;
import ru.practicum.shareit.dto.ItemDto;

import java.util.Collections;

@Component
public class ItemClient extends BaseClient {

    public ItemClient() {
        super("/items");
    }

    public Object addItem(ItemDto itemDto, Long ownerId) {
        return post("", itemDto, ownerId);
    }

    public Object updateItem(long itemId, ItemDto itemDto, long ownerId) {
        return patch("/" + itemId, itemDto, ownerId);
    }

    public Object getItem(long itemId) {
        return get("/" + itemId);
    }

    public Object getAllOwnerItems(long ownerId) {
        return get("", ownerId);
    }

    public Object getNecessaryItem(String text) {
        if (text == null || text.isBlank()) {
            return get("/search", null, Collections.emptyMap());
        }

        return get("/search?text={text}", null, Collections.singletonMap("text", text));
    }

    public Object addComment(long itemId, long ownerId, CommentDto comment) {
        return post("/" + itemId + "/comment", comment, ownerId);
    }
}
