package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.Collection;
import java.util.List;

public class BookingServiceImpl implements BookingService{
    @Override
    public Booking createBooking(Long bookerId, BookingDto bookingDto) {
        return null;
    }

    @Override
    public Booking updateBookingStatus(Long userId, Long bookingId, Boolean approved) {
        return null;
    }

    @Override
    public Booking getBooking(Long userId, Long bookingId) {
        return null;
    }

    @Override
    public Collection<Booking> findByBookerAndState(Long bookerId, BookingStatus state) {
        return List.of();
    }

    @Override
    public Collection<Booking> findByOwnerAndState(Long ownerId, BookingStatus state) {
        return List.of();
    }
}
