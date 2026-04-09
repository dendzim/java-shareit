package ru.practicum.shareit.client;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import ru.practicum.shareit.dto.BookingDto;
import ru.practicum.shareit.dto.BookingState;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    public BookingClient(RestTemplate rest) {
        super(rest);
    }

    public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public Object createBooking(Long userId, BookingDto bookingDto) {
        return null;
    }

    public Object updateBookingStatus(Long userId, Long bookingId, Boolean approved) {
        return null;
    }

    public Collection<Object> findByBookerAndState(Long bookerId, BookingState state) {
        return null;
    }

    public Collection<Object> findByOwnerAndState(Long ownerId, BookingState state) {
        return null;
    }
}
