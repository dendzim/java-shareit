package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public Booking createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @RequestBody BookingDto bookingDto) {

        return bookingService.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public Booking updateBookingStatus(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved) {

        return bookingService.updateBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking getBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId) {

        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public Collection<Booking> findByBookerAndState(
            @RequestHeader("X-Sharer-User-Id") Long bookerId,
            @RequestParam (defaultValue = "ALL") BookingState state) {

        return bookingService.findByBookerAndState(bookerId, state);
    }

    @GetMapping("/owner")
    public Collection<Booking> findByOwnerAndState(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam (defaultValue = "ALL") BookingState state) {

        return bookingService.findByOwnerAndState(ownerId, state);
    }
}
