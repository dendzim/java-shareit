package ru.practicum.shareit.client;

import java.util.Collections;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ru.practicum.shareit.dto.BookingDto;
import ru.practicum.shareit.dto.BookingState;

@Component
public class BookingClient extends BaseClient {

    public BookingClient() {
        super("/bookings");
    }

    public Object getBooking(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public Object createBooking(Long userId, BookingDto bookingDto) {
        return post("", userId, bookingDto);
    }

    public Object updateBookingStatus(Long userId, Long bookingId, Boolean approved) {
        return patch("/" + bookingId + "?approved={approved}",
                userId,
                Collections.singletonMap("approved", approved));
    }

    public Object findByBookerAndState(Long bookerId, BookingState state) {
        return get("?state={state}", bookerId, Collections.singletonMap("state", state));
    }

    public Object findByOwnerAndState(Long ownerId, BookingState state) {
        return get("/owner?state={state}", ownerId, Collections.singletonMap("state", state));
    }
}
