package ru.practicum.shareit.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.OnCreate;
import ru.practicum.shareit.OnUpdate;
import ru.practicum.shareit.client.ItemClient;
import ru.practicum.shareit.dto.CommentDto;
import ru.practicum.shareit.dto.ItemDto;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public Object addItem(@Validated(OnCreate.class) @RequestBody final ItemDto itemDto,
                           @RequestHeader("X-Sharer-User-Id") @Positive Long ownerId) {
        log.info("Предмет добавлен");
        return itemClient.addItem(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public Object updateItem(@Positive @PathVariable("itemId") final long itemId,
                              @RequestHeader("X-Sharer-User-Id") @Positive long ownerId,
                              @Validated(OnUpdate.class) @RequestBody final ItemDto itemDto) {
        log.info("Предмет обновлен");
        return itemClient.updateItem(itemId, itemDto,  ownerId);
    }

    @GetMapping("/{itemId}")
    public Object getItem(@Positive @PathVariable("itemId") final long itemId) {
        log.info("Предмет выведен");
        return itemClient.getItem(itemId);
    }

    @GetMapping
    public Object getAllOwnerItems(@RequestHeader("X-Sharer-User-Id") @Positive long ownerId) {
        log.info("Список всех предметов владельца выведен");
        return itemClient.getAllOwnerItems(ownerId);
    }

    @GetMapping("/search")
    public Object getNecessaryItem(@RequestParam String text) {
        log.info("Список предметов содержащих " + text + " выведен");
        return itemClient.getNecessaryItem(text);
    }

    @PostMapping("/{itemId}/comment")
    public Object addComment(@Positive @PathVariable("itemId") final long itemId,
                                 @RequestHeader("X-Sharer-User-Id") @Positive long ownerId,
                             @Valid @RequestBody CommentDto comment) {
        log.info("Комментарий добавлен");
        return itemClient.addComment(itemId, ownerId, comment);
    }
}
