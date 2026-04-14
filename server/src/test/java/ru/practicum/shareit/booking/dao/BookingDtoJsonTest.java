package ru.practicum.shareit.booking.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoJsonTest {

    @Autowired
    private JacksonTester<BookingDto> jacksonTester;

    @Autowired
    private ObjectMapper objectMapper;

    private BookingDto bookingDto;
    private LocalDateTime start;
    private LocalDateTime end;

    @BeforeEach
    void setUp() {
        start = LocalDateTime.of(2022, 2, 2, 10, 0, 0);
        end = LocalDateTime.of(2022, 3, 5, 18, 30, 0);

        bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(start);
        bookingDto.setEnd(end);
    }

    @Test
    void shouldSerializeBookingDto() throws IOException {
        JsonContent<BookingDto> json = jacksonTester.write(bookingDto);

        assertThat(json).hasJsonPathNumberValue("$.itemId");
        assertThat(json).hasJsonPathStringValue("$.start");
        assertThat(json).hasJsonPathStringValue("$.end");

        assertThat(json).extractingJsonPathNumberValue("$.itemId")
                .isEqualTo(1);

        assertThat(json).extractingJsonPathStringValue("$.start")
                .satisfies(startStr -> {
                    assertThat(startStr).startsWith(start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                });

        assertThat(json).extractingJsonPathStringValue("$.end")
                .satisfies(endStr -> {
                    assertThat(endStr).startsWith(end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                });
    }

    @Test
    void shouldDeserializeBookingDto() throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        String json = String.format(
                "{" +
                        "\"itemId\":1," +
                        "\"start\":\"%s\"," +
                        "\"end\":\"%s\"" +
                        "}",
                start.format(formatter),
                end.format(formatter)
        );

        BookingDto deserializedBooking = jacksonTester.parseObject(json);

        assertThat(deserializedBooking.getItemId()).isEqualTo(1L);
        assertThat(deserializedBooking.getStart().withNano(0))
                .isEqualTo(start.withNano(0));
        assertThat(deserializedBooking.getEnd().withNano(0))
                .isEqualTo(end.withNano(0));
    }

    @Test
    void shouldDeserializeBookingDtoWithNullFields() throws IOException {
        String json = "{" +
                "\"itemId\":null," +
                "\"start\":null," +
                "\"end\":null" +
                "}";

        BookingDto deserializedBooking = jacksonTester.parseObject(json);

        assertThat(deserializedBooking.getItemId()).isNull();
        assertThat(deserializedBooking.getStart()).isNull();
        assertThat(deserializedBooking.getEnd()).isNull();
    }
}
