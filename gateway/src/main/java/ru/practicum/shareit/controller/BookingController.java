package ru.practicum.shareit.controller;

import jakarta.validation.constraints.Positive;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.dto.BookingDto;
import ru.practicum.shareit.dto.BookingState;
import ru.practicum.shareit.client.BookingClient;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public Object createBooking(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                 @RequestBody BookingDto bookingDto) {

        return bookingClient.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public Object updateBookingStatus(
            @RequestHeader("X-Sharer-User-Id") @Positive Long userId,
            @Positive @PathVariable Long bookingId,
            @RequestParam Boolean approved) {

        return bookingClient.updateBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public Object getBooking(
            @RequestHeader("X-Sharer-User-Id") @Positive Long userId,
            @PathVariable Long bookingId) {

        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public Object findByBookerAndState(
            @RequestHeader("X-Sharer-User-Id") @Positive Long bookerId,
            @RequestParam (defaultValue = "ALL") BookingState state) {

        return bookingClient.findByBookerAndState(bookerId, state);
    }

    @GetMapping("/owner")
    public Object findByOwnerAndState(
            @RequestHeader("X-Sharer-User-Id") @Positive Long ownerId,
            @RequestParam (defaultValue = "ALL") BookingState state) {

        return bookingClient.findByOwnerAndState(ownerId, state);
    }
}
