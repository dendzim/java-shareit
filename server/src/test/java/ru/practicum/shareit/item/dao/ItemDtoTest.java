package ru.practicum.shareit.item.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemDtoTest {

    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();
    }

    @Test
    void testNoArgsConstructor() {
        assertNotNull(itemDto);
        assertNull(itemDto.getId());
        assertNull(itemDto.getName());
        assertNull(itemDto.getDescription());
        assertNull(itemDto.getAvailable());
        assertNull(itemDto.getLastBooking());
        assertNull(itemDto.getNextBooking());
        assertNull(itemDto.getComments());
        assertNull(itemDto.getRequestId());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        String name = "Test Item";
        String description = "Test Description";
        Boolean available = true;
        LocalDateTime lastBooking = LocalDateTime.now().minusDays(1);
        LocalDateTime nextBooking = LocalDateTime.now().plusDays(1);
        Collection<CommentDto> comments = List.of(new CommentDto());
        Long requestId = 10L;

        ItemDto dto = new ItemDto(id, name, description, available,
                lastBooking, nextBooking, comments, requestId);

        assertEquals(id, dto.getId());
        assertEquals(name, dto.getName());
        assertEquals(description, dto.getDescription());
        assertEquals(available, dto.getAvailable());
        assertEquals(lastBooking, dto.getLastBooking());
        assertEquals(nextBooking, dto.getNextBooking());
        assertEquals(comments, dto.getComments());
        assertEquals(requestId, dto.getRequestId());
    }
}
