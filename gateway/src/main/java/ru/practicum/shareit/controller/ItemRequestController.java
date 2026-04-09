package ru.practicum.shareit.controller;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.ItemRequestClient;
import ru.practicum.shareit.dto.ItemRequestDto;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public Object addRequest(@RequestBody final ItemRequestDto itemRequest) {
        log.info("Запрос добавлен");
        return itemRequestClient.addItemRequest(itemRequest);
    }

    @GetMapping("/all")
    public Object getAllRequests() {
        log.info("Список запросов выведен");
        return itemRequestClient.getAllRequests();
    }

    @GetMapping
    public Object getMyReqests(@RequestHeader("X-Sharer-User-Id") @Positive Long ownerId) {
        log.info("Запросы пользователя с id " + ownerId + " выведен");
        return itemRequestClient.getMyRequests(ownerId);
    }

    @GetMapping("/{requestId}")
    public Object getReqestById(@Positive @PathVariable("requestId") final Long id) {
        log.info("Запрос с id " + id + " выведен");
        return itemRequestClient.getReqestById(id);
    }
}
