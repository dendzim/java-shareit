package ru.practicum.shareit.item.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.CommentDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ItemDto> jacksonTester;

    @Autowired
    private ObjectMapper objectMapper;

    private ItemDto itemDto;
    private LocalDateTime now;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.of(2025, 3, 3, 12, 0, 0);

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("testItem");
        commentDto.setAuthorName("User");
        commentDto.setCreated(now);

        Collection<CommentDto> comments = List.of(commentDto);

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Item");
        itemDto.setDescription("Description");
        itemDto.setAvailable(true);
        itemDto.setLastBooking(now.minusDays(1));
        itemDto.setNextBooking(now.plusDays(1));
        itemDto.setComments(comments);
        itemDto.setRequestId(10L);
    }

    @Test
    void shouldSerializeItemDto() throws IOException {
        JsonContent<ItemDto> json = jacksonTester.write(itemDto);

        assertThat(json).hasJsonPathNumberValue("$.id");
        assertThat(json).hasJsonPathStringValue("$.name");
        assertThat(json).hasJsonPathStringValue("$.description");
        assertThat(json).hasJsonPathBooleanValue("$.available");
        assertThat(json).hasJsonPathStringValue("$.lastBooking");
        assertThat(json).hasJsonPathStringValue("$.nextBooking");
        assertThat(json).hasJsonPathArrayValue("$.comments");
        assertThat(json).hasJsonPathNumberValue("$.requestId");

        assertThat(json).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(json).extractingJsonPathStringValue("$.name")
                .isEqualTo("Item");
        assertThat(json).extractingJsonPathStringValue("$.description")
                .isEqualTo("Description");
        assertThat(json).extractingJsonPathBooleanValue("$.available")
                .isTrue();
        assertThat(json).extractingJsonPathStringValue("$.lastBooking")
                .satisfies(lastBookingStr -> {
                    assertThat(lastBookingStr).startsWith(now.minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                });

        assertThat(json).extractingJsonPathStringValue("$.nextBooking")
                .satisfies(nextBookingStr -> {
                    assertThat(nextBookingStr).startsWith(now.plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                });

        assertThat(json).extractingJsonPathNumberValue("$.requestId")
                .isEqualTo(10);
        assertThat(json).extractingJsonPathArrayValue("$.comments")
                .hasSize(1);
    }

    @Test
    void shouldDeserializeItemDto() throws IOException {
        String json = String.format(
                "{" +
                        "\"id\":1," +
                        "\"name\":\"Item\"," +
                        "\"description\":\"Description\"," +
                        "\"available\":true," +
                        "\"lastBooking\":\"%s\"," +
                        "\"nextBooking\":\"%s\"," +
                        "\"comments\":[" +
                        "{" +
                        "\"id\":1," +
                        "\"text\":\"testItem\"," +
                        "\"authorName\":\"User\"," +
                        "\"created\":\"%s\"" +
                        "}" +
                        "]," +
                        "\"requestId\":10" +
                        "}",
                now.minusDays(1).toString(),
                now.plusDays(1).toString(),
                now.toString()
        );

        ItemDto deserializedItem = jacksonTester.parseObject(json);

        assertThat(deserializedItem.getId()).isEqualTo(1L);
        assertThat(deserializedItem.getName()).isEqualTo("Item");
        assertThat(deserializedItem.getDescription()).isEqualTo("Description");
        assertThat(deserializedItem.getAvailable()).isTrue();
        assertThat(deserializedItem.getLastBooking()).isEqualTo(now.minusDays(1));
        assertThat(deserializedItem.getNextBooking()).isEqualTo(now.plusDays(1));
        assertThat(deserializedItem.getRequestId()).isEqualTo(10L);
        assertThat(deserializedItem.getComments()).hasSize(1);

        CommentDto deserializedComment = deserializedItem.getComments().iterator().next();
        assertThat(deserializedComment.getId()).isEqualTo(1L);
        assertThat(deserializedComment.getText()).isEqualTo("testItem");
        assertThat(deserializedComment.getAuthorName()).isEqualTo("User");
        assertThat(deserializedComment.getCreated()).isEqualTo(now);
    }

    @Test
    void shouldDeserializeItemDtoWithNullFields() throws IOException {
        String json = "{" +
                "\"id\":null," +
                "\"name\":null," +
                "\"description\":null," +
                "\"available\":null," +
                "\"lastBooking\":null," +
                "\"nextBooking\":null," +
                "\"comments\":null," +
                "\"requestId\":null" +
                "}";

        ItemDto deserializedItem = jacksonTester.parseObject(json);

        assertThat(deserializedItem.getId()).isNull();
        assertThat(deserializedItem.getName()).isNull();
        assertThat(deserializedItem.getDescription()).isNull();
        assertThat(deserializedItem.getAvailable()).isNull();
        assertThat(deserializedItem.getLastBooking()).isNull();
        assertThat(deserializedItem.getNextBooking()).isNull();
        assertThat(deserializedItem.getComments()).isNull();
        assertThat(deserializedItem.getRequestId()).isNull();
    }

    @Test
    void shouldDeserializeItemDtoWithEmptyComments() throws IOException {
        String json = "{" +
                "\"id\":1," +
                "\"name\":\"Item\"," +
                "\"description\":\"Description\"," +
                "\"available\":true," +
                "\"lastBooking\":null," +
                "\"nextBooking\":null," +
                "\"comments\":[]," +
                "\"requestId\":null" +
                "}";

        ItemDto deserializedItem = jacksonTester.parseObject(json);

        assertThat(deserializedItem.getId()).isEqualTo(1L);
        assertThat(deserializedItem.getName()).isEqualTo("Item");
        assertThat(deserializedItem.getDescription()).isEqualTo("Description");
        assertThat(deserializedItem.getAvailable()).isTrue();
        assertThat(deserializedItem.getLastBooking()).isNull();
        assertThat(deserializedItem.getNextBooking()).isNull();
        assertThat(deserializedItem.getComments()).isEmpty();
        assertThat(deserializedItem.getRequestId()).isNull();
    }
}
