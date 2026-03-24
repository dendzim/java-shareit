package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@Setter
@Getter
@AllArgsConstructor
public class BookingDto {
    @NotNull
    @Positive
    private Long itemId;
    @FutureOrPresent
    private LocalDate start;
    @FutureOrPresent
    private LocalDate end;

}
