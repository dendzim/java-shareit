package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dao.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemRequestMapperTest {

    @Test
    public void shouldMapItemRequestToItemRequestDto() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(10L);
        itemRequest.setDescription("Нужен молоток");
        LocalDate created = LocalDate.of(2026, 4, 13);
        itemRequest.setCreated(created);

        ItemRequestDto dto = ItemRequestMapper.mapToItemRequestDto(itemRequest);

        assertEquals(itemRequest.getId(), dto.getId());
        assertEquals(itemRequest.getDescription(), dto.getDescription());
    }
}
