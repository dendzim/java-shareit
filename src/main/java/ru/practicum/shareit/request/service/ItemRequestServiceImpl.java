package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;
import java.util.List;

public class ItemRequestServiceImpl implements ItemRequestService {
    @Override
    public ItemRequestDto addItemRequest(ItemRequest itemRequest) {
        return null;
    }

    @Override
    public Collection<ItemRequestDto> getAllRequests() {
        return List.of();
    }

    @Override
    public Collection<ItemRequestDto> getMyReqests(Long ownerId) {
        return List.of();
    }

    @Override
    public void getReqestById(Long id) {

    }
}
