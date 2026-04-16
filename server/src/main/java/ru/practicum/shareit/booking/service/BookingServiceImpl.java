package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

import static ru.practicum.shareit.booking.dao.BookingMapper.mapToBooking;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public Booking createBooking(Long bookerId, BookingDto bookingDto) {
        if (!bookingDto.getStart().isBefore(bookingDto.getEnd())) {
            throw new BadRequestException("Начало не может быть после конца бронирования");
        }
        User user = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + bookerId + " не найден"));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Предмет с id " + bookingDto.getItemId() + " не найден"));
        if (!item.getIsAvailable()) {
            throw new BadRequestException("Предмет уже занят");
        }

        Booking booking = mapToBooking(bookingDto);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking updateBookingStatus(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
        if (!booking.getItem().getOwnerId().equals(userId)) {
            throw new BadRequestException("Менять статус может только владелец");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwnerId().equals(userId)) {
            throw new ValidationException("Только владелец или арендатор могут посмотреть бронирование");
        }
        return booking;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Booking> findByBookerAndState(Long bookerId, BookingState state) {
        if (userRepository.findById(bookerId).isEmpty()) {
                throw new NotFoundException("Пользователь с id " + bookerId + " не найден");
        }
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case CURRENT:
                return bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsBeforeOrderByEndDesc(bookerId, now, now);
            case REJECTED:
                return bookingRepository.findByBookerIdAndStatusIsOrderByEndDesc(bookerId, BookingStatus.REJECTED);
            case WAITING:
                return bookingRepository.findByBookerIdAndStatusIsOrderByEndDesc(bookerId, BookingStatus.WAITING);
            case PAST:
                return bookingRepository.findByBookerIdAndEndIsBeforeOrderByEndDesc(bookerId, now);
            case FUTURE:
                return bookingRepository.findByBookerIdAndStartIsAfterOrderByEndDesc(bookerId, now);
        }
        return bookingRepository.findByBookerIdOrderByEndDesc(bookerId);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Booking> findByOwnerAndState(Long ownerId, BookingState state) {
        if (userRepository.findById(ownerId).isEmpty()) {
            throw new NotFoundException("Пользователь с id " + ownerId + " не найден");
        }
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case CURRENT:
                return bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsBeforeOrderByEndDesc(ownerId, now,
                        now);
            case REJECTED:
                return bookingRepository.findByItemOwnerIdAndStatusIsOrderByEndDesc(ownerId, BookingStatus.REJECTED);
            case WAITING:
                return bookingRepository.findByItemOwnerIdAndStatusIsOrderByEndDesc(ownerId, BookingStatus.WAITING);
            case PAST:
                return bookingRepository.findByItemOwnerIdAndEndIsBeforeOrderByEndDesc(ownerId, now);
            case FUTURE:
                return bookingRepository.findByItemOwnerIdAndStartIsAfterOrderByEndDesc(ownerId, now);
        }
        return bookingRepository.findByItemOwnerIdOrderByEndDesc(ownerId);
    }
}
