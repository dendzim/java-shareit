package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private final LocalDateTime now = LocalDateTime.now();

    @Test
    public void shouldCreateBooking() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(now.plusDays(1));
        bookingDto.setEnd(now.plusDays(2));

        User user = new User(1L, "booker", "booker@example.com");
        Item item = new Item(1L, "item", "description", true, 2L, null);
        Booking savedBooking = new Booking();
        savedBooking.setId(1L);
        savedBooking.setStatus(BookingStatus.WAITING);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(bookingRepository.save(any())).thenReturn(savedBooking);

        Booking result = bookingService.createBooking(1L, bookingDto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo(BookingStatus.WAITING);
        verify(bookingRepository).save(any());
    }

    @Test
    public void shouldThrowBadRequestExceptionWhenCreateBookingForUnavailableItem() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(now.plusDays(1));
        bookingDto.setEnd(now.plusDays(2));

        User user = new User(1L, "booker", "booker@example.com");
        Item item = new Item(1L, "item", "description", false, 2L, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> bookingService.createBooking(1L, bookingDto))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    public void shouldGetBooking() {
        User booker = new User(1L, "booker", "booker@example.com");
        Item item = new Item(1L, "item", "description", true, 2L, null);
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(booker);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));  // ← убрали пробел после (

        Booking result = bookingService.getBooking(1L, 1L);

        assertThat(result.getId()).isEqualTo(1L);
        verify(bookingRepository).findById(1L);
    }
}
