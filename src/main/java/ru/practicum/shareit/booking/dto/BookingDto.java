package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@AllArgsConstructor
public class BookingDto {
    private LocalDate start;
    private LocalDate end;
    private Item item;
    private User booker;
    private BookingStatus status;
}
