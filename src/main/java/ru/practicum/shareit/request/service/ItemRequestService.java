package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(ItemRequest itemRequest);

    Collection<ItemRequestDto> getAllRequests();

    Collection<ItemRequestDto> getMyReqests(Long ownerId);

    void getReqestById(Long id);
}
