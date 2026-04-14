package ru.practicum.shareit.booking.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingDtoTest {

    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        bookingDto = new BookingDto();
    }

    @Test
    void testNoArgsConstructor() {
        assertNotNull(bookingDto);
        assertNull(bookingDto.getItemId());
        assertNull(bookingDto.getStart());
        assertNull(bookingDto.getEnd());
    }

    @Test
    void testAllArgsConstructor() {
        Long itemId = 1L;
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        BookingDto dto = new BookingDto(itemId, start, end);

        assertEquals(itemId, dto.getItemId());
        assertEquals(start, dto.getStart());
        assertEquals(end, dto.getEnd());
    }
}
