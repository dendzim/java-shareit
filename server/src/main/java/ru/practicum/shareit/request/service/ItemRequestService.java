package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(ItemRequest itemRequest, Long userId);

    Collection<ItemRequestDto> getAllRequests();

    Collection<ItemRequestDto> getMyRequests(Long ownerId);

    ItemRequestDto getRequestById(Long id);
}
