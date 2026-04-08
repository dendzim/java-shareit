package ru.practicum.shareit.request.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;

import java.util.Collection;

/**
 * TODO Sprint add-item-requests.
 */
@Slf4j
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {

    private final ItemRequestServiceImpl itemRequestServiceImpl;

    @PostMapping
    public ItemRequestDto addUser(@RequestBody final ItemRequest itemRequest) {
        log.info("Запрос добавлен");
        return itemRequestServiceImpl.addItemRequest(itemRequest);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAllRequests() {
        log.info("Список запросов выведен");
        return itemRequestServiceImpl.getAllRequests();
    }

    @GetMapping
    public Collection<ItemRequestDto> getMyReqests(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Запросы пользователя с id " + ownerId + " выведен");
        return itemRequestServiceImpl.getMyReqests(ownerId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getReqestById(@PathVariable("requestId") final Long id) {
        log.info("Запрос с id " + id + " выведен");
        return itemRequestServiceImpl.getReqestById(id);
    }
}
