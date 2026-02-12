package ru.practicum.shareit.booking;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;

@UtilityClass
public class BookingMapper {
    public static BookingDto mapToBooking(Booking booking) {
        return new BookingDto(
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus()
        );
    }
}
