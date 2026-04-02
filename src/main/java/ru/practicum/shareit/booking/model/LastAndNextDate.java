package ru.practicum.shareit.booking.model;

import java.time.LocalDateTime;

public interface LastAndNextDate {
    Long getItemId();

    LocalDateTime getLastBooking();

    LocalDateTime getNextBooking();
}
