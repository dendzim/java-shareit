package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.validation.OnCreate;
import ru.practicum.shareit.validation.OnUpdate;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@Validated(OnCreate.class) @RequestBody final ItemDto itemDto,
                           @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Предмет добавлен");
        return itemService.addItem(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable("itemId")  final long itemId,
                              @RequestHeader("X-Sharer-User-Id") long ownerId,
                              @Validated(OnUpdate.class) @RequestBody final ItemDto itemDto) {
        itemDto.setId(itemId);
        log.info("Предмет обновлен");
        return itemService.updateItem(itemDto, ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable("itemId") final long itemId) {
        log.info("Предмет выведен");
        return itemService.getItem(itemId);
    }

    @GetMapping
    public Collection<ItemDto> getAllOwnerItems(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        log.info("Список всех предметов владельца выведен");
        return itemService.getAllOwnerItems(ownerId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> getNecessaryItem(@RequestParam String text) {
        log.info("Список предметов содержащих " + text + " выведен");
        return itemService.getNecessaryItem(text);
    }
}
