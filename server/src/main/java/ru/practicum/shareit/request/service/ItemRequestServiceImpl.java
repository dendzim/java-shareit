package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.ItemMapper;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestMapper;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto addItemRequest(ItemRequest itemRequest, Long userId) {
        itemRequest.setRequestorId(userId);
        return ItemRequestMapper.mapToItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<ItemRequestDto> getAllRequests() {
        Collection<ItemRequest> itemRequest = itemRequestRepository.findAllByOrderByCreatedDesc();
        return addItemsToRequest(itemRequest);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<ItemRequestDto> getMyRequests(Long ownerId) {
        Collection<ItemRequest> itemRequest = itemRequestRepository.findByRequestorIdOrderByCreatedDesc(ownerId);
        return addItemsToRequest(itemRequest);
    }

    @Transactional
    @Override
    public ItemRequestDto getRequestById(Long id) {
        ItemRequest itemRequest = itemRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Запрос с id = " + id + " не найден"));

        List<ItemDto> items = itemRepository.findByRequestId(id)
                .stream()
                .map(ItemMapper::toItemDto)
                .toList();

        ItemRequestDto itemRequestDto = ItemRequestMapper.mapToItemRequestDto(itemRequest);
        itemRequestDto.setItems(items);

        return itemRequestDto;
    }

    private List<ItemRequestDto> addItemsToRequest(Collection<ItemRequest> itemRequests) {
        List<Long> requestIds = itemRequests.stream()
                .map(ItemRequest::getId)
                .toList();

        List<Item> items = itemRepository.findAllByRequestIdIn(requestIds);

        Map<Long, List<ItemDto>> itemsByRequestId = items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.groupingBy(ItemDto::getRequestId));

        return itemRequests.stream()
                .map(request -> {
                    ItemRequestDto dto = ItemRequestMapper.mapToItemRequestDto(request);
                    dto.setItems(itemsByRequestId.getOrDefault(request.getId(), List.of()));
                    return dto;
                })
                .toList();
    }
}
