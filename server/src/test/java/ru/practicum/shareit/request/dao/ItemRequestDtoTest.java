package ru.practicum.shareit.request.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class ItemRequestDtoTest {

    private ItemRequestDto itemRequestDto;
    private LocalDate testDate;
    private List<ItemDto> testItems;

    @BeforeEach
    void setUp() {
        itemRequestDto = new ItemRequestDto();
        testDate = LocalDate.now();

        ItemDto item1 = new ItemDto(1L, "name", "desc", true, null,
                null, null, null);
        ItemDto item2 = new ItemDto(2L, "name", "desc", true, null,
                null, null, null);

        testItems = List.of(item1, item2);
    }

    @Test
    void testNoArgsConstructor() {
        assertNotNull(itemRequestDto);
        assertNull(itemRequestDto.getId());
        assertNull(itemRequestDto.getItems());
        assertNull(itemRequestDto.getDescription());
        assertNull(itemRequestDto.getCreated());
    }

    @Test
    void testAllArgsConstructor() {
        ItemRequestDto dto = new ItemRequestDto(1L, "Description", testDate, testItems);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getDescription()).isEqualTo("Description");
        assertThat(dto.getCreated()).isEqualTo(testDate);
        assertThat(dto.getItems()).isEqualTo(testItems);
    }
}
