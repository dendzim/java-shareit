package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.LastAndNextDate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

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

    boolean existsByItemIdAndBookerIdAndStatusIsAndEndBefore(Long itemId, Long userId, BookingStatus bookingStatus,
                                                             LocalDateTime now);

    @Query("""
                SELECT
                    b.item.id as itemId,
                    MAX(CASE WHEN b.start < CURRENT_TIMESTAMP THEN b.start ELSE NULL END) as lastBooking,
                    MIN(CASE WHEN b.start > CURRENT_TIMESTAMP THEN b.start ELSE NULL END) as nextBooking
                FROM Booking b
                WHERE b.item.ownerId = ?1
                GROUP BY b.item.id
            """)
    List<LastAndNextDate> findLastAndNextDatesByOwnerId(long ownerId);
}
