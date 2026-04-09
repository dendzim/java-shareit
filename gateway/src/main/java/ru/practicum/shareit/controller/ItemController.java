package ru.practicum.shareit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.OnCreate;
import ru.practicum.shareit.OnUpdate;
import ru.practicum.shareit.client.ItemClient;
import ru.practicum.shareit.dto.CommentDto;
import ru.practicum.shareit.dto.ItemDto;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public Object addItem(@Validated(OnCreate.class) @RequestBody final ItemDto itemDto,
                           @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Предмет добавлен");
        return itemClient.addItem(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public Object updateItem(@PathVariable("itemId") final long itemId,
                              @RequestHeader("X-Sharer-User-Id") long ownerId,
                              @Validated(OnUpdate.class) @RequestBody final ItemDto itemDto) {
        log.info("Предмет обновлен");
        return itemClient.updateItem(itemDto, itemId, ownerId);
    }

    @GetMapping("/{itemId}")
    public Object getItem(@PathVariable("itemId") final long itemId) {
        log.info("Предмет выведен");
        return itemClient.getItem(itemId);
    }

    @GetMapping
    public Object getAllOwnerItems(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        log.info("Список всех предметов владельца выведен");
        return itemClient.getAllOwnerItems(ownerId);
    }

    @GetMapping("/search")
    public Object getNecessaryItem(@RequestParam String text) {
        log.info("Список предметов содержащих " + text + " выведен");
        return itemClient.getNecessaryItem(text);
    }

    @PostMapping("/{itemId}/comment")
    public Object addComment(@PathVariable("itemId") final long itemId,
                                 @RequestHeader("X-Sharer-User-Id") long ownerId,
                                 @RequestBody CommentDto comment) {
        log.info("Комментарий добавлен");
        return itemClient.addComment(ownerId, itemId, comment);
    }
}
