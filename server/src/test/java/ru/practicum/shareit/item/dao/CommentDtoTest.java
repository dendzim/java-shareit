package ru.practicum.shareit.item.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentDtoTest {

    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        commentDto = new CommentDto();
    }

    @Test
    void testNoArgsConstructor() {
        assertNotNull(commentDto);
        assertNull(commentDto.getId());
        assertNull(commentDto.getItemId());
        assertNull(commentDto.getAuthorName());
        assertNull(commentDto.getText());
        assertNull(commentDto.getCreated());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        Long itemId = 10L;
        String authorName = "John Doe";
        String text = "Great item!";
        LocalDateTime created = LocalDateTime.now();

        CommentDto dto = new CommentDto(id, itemId, authorName, text, created);

        assertEquals(id, dto.getId());
        assertEquals(itemId, dto.getItemId());
        assertEquals(authorName, dto.getAuthorName());
        assertEquals(text, dto.getText());
        assertEquals(created, dto.getCreated());
    }
}