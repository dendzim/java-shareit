package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Collection<Booking> findByBookerIdOrderByEndDesc(Long bookerId);

    Collection<Booking> findByItemOwnerIdOrderByEndDesc(Long ownerId);

    Collection<Booking> findByBookerIdAndStartIsBeforeAndEndIsBeforeOrderByEndDesc(Long bookerId, LocalDateTime now,
                                                                                   LocalDateTime now1);

    Collection<Booking> findByBookerIdAndStatusIsOrderByEndDesc(Long bookerId, BookingStatus bookingStatus);

    Collection<Booking> findByBookerIdAndEndIsBeforeOrderByEndDesc(Long bookerId, LocalDateTime now);

    Collection<Booking> findByBookerIdAndStartIsAfterOrderByEndDesc(Long bookerId, LocalDateTime now);

    Collection<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsBeforeOrderByEndDesc(Long ownerId, LocalDateTime now,
                                                                                      LocalDateTime now1);

    Collection<Booking> findByItemOwnerIdAndStatusIsOrderByEndDesc(Long ownerId, BookingStatus bookingStatus);

    Collection<Booking> findByItemOwnerIdAndEndIsBeforeOrderByEndDesc(Long ownerId, LocalDateTime now);

    Collection<Booking> findByItemOwnerIdAndStartIsAfterOrderByEndDesc(Long ownerId, LocalDateTime now);
}
