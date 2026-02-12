package ru.practicum.shareit.item;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@RequestBody final Item item, @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.addItem(item, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable("itemId")  final long itemId, @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.updateItem(itemId, ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable("itemId") final long itemId, @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.getItem(itemId);
    }

    @GetMapping
    public Collection<ItemDto> getAllOwnerItems(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.getAllOwnerItems(ownerId);
    }

    @GetMapping("//search?text={text}")
    public Collection<ItemDto> getNecessaryItem(@RequestHeader("X-Sharer-User-Id") long ownerId, @RequestParam("text") String text) {
        return itemService.getNecessaryItem(text);
    }
}
