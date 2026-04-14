package ru.practicum.shareit.booking.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.LastAndNextDate;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingRepositoryTest {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private User owner;
    private User booker;
    private Item item;

    @BeforeEach
    void setUp() {
        User testUser1 = new User(null, "name1", "email1@test.com");
        User testUser2 = new User(null, "name2", "email2@test.com");
        owner = userRepository.save(testUser1);
        booker = userRepository.save(testUser2);
        item = itemRepository.save(Item.builder()
                .name("item")
                .description("desc")
                .isAvailable(true)
                .ownerId(owner.getId())
                .build());
    }

    @Test
    public void shouldFindLastAndNextDatesByOwnerId() {
        Booking pastBooking = bookingRepository.save(Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build());

        List<LastAndNextDate> dates = bookingRepository.findLastAndNextDatesByOwnerId(owner.getId());

        assertThat(dates).hasSize(1);
        assertThat(dates.getFirst().getItemId()).isEqualTo(item.getId());
    }

    @Test
    public void shouldExistsByItemIdAndBookerIdAndStatusIsAndEndBefore() {
        bookingRepository.save(Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build());

        Boolean exists = bookingRepository.existsByItemIdAndBookerIdAndStatusIsAndEndBefore(
                item.getId(),
                booker.getId(),
                BookingStatus.APPROVED,
                LocalDateTime.now()
        );

        assertThat(exists).isTrue();
    }

    @Test
    public void shouldFindByBookerIdOrderByEndDesc() {
        Booking booking1 = bookingRepository.save(Booking.builder()
                .start(LocalDateTime.now().plusDays(3))
                .end(LocalDateTime.now().plusDays(4))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build());

        Booking booking2 = bookingRepository.save(Booking.builder()
                .start(LocalDateTime.now().plusDays(5))
                .end(LocalDateTime.now().plusDays(6))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build());

        List<Booking> bookings = bookingRepository.findByBookerIdOrderByEndDesc(booker.getId());

        assertThat(bookings).hasSize(2);
        assertThat(bookings.getFirst().getEnd()).isAfter(bookings.get(1).getEnd());
    }

    @Test
    public void shouldFindByBookerIdAndStatusIsOrderByEndDesc() {
        bookingRepository.save(Booking.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build());

        List<Booking> bookings = bookingRepository
                .findByBookerIdAndStatusIsOrderByEndDesc(booker.getId(), BookingStatus.WAITING);

        assertThat(bookings).hasSize(1);
        assertThat(bookings.getFirst().getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    public void shouldFindByBookerIdAndStartIsBeforeAndEndIsBeforeOrderByEndDesc() {
        bookingRepository.save(Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build());

        List<Booking> bookings = bookingRepository
                .findByBookerIdAndStartIsBeforeAndEndIsBeforeOrderByEndDesc(
                booker.getId(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        assertThat(bookings).hasSize(1);
    }

    @Test
    public void shouldFindByBookerIdAndStartIsAfterOrderByEndDesc() {
        bookingRepository.save(Booking.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build());

        List<Booking> bookings = bookingRepository.findByBookerIdAndStartIsAfterOrderByEndDesc(
                booker.getId(),
                LocalDateTime.now()
        );

        assertThat(bookings).hasSize(1);
    }

    @Test
    public void shouldFindByItemOwnerIdAndStartIsAfterOrderByEndDesc() {
        bookingRepository.save(Booking.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build());

        List<Booking> bookings = bookingRepository.findByItemOwnerIdAndStartIsAfterOrderByEndDesc(
                owner.getId(),
                LocalDateTime.now()
        );

        assertThat(bookings).hasSize(1);
    }

    @Test
    public void shouldFindByBookerIdAndEndIsBeforeOrderByEndDesc() {
        bookingRepository.save(Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build());

        List<Booking> bookings = bookingRepository.findByBookerIdAndEndIsBeforeOrderByEndDesc(
                booker.getId(),
                LocalDateTime.now()
        );

        assertThat(bookings).hasSize(1);
    }

    @Test
    public void shouldFindByItemOwnerIdOrderByEndDesc() {
        bookingRepository.save(Booking.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build());

        List<Booking> bookings = bookingRepository.findByItemOwnerIdOrderByEndDesc(owner.getId());

        assertThat(bookings).hasSize(1);
    }

    @Test
    public void shouldFindByItemOwnerIdAndStatusIsOrderByEndDesc() {
        bookingRepository.save(Booking.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build());

        List<Booking> bookings = bookingRepository.findByItemOwnerIdAndStatusIsOrderByEndDesc(
                owner.getId(),
                BookingStatus.WAITING
        );

        assertThat(bookings).hasSize(1);
        assertThat(bookings.getFirst().getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    public void shouldFindByItemOwnerIdAndStartIsBeforeAndEndIsBeforeOrderByEndDesc() {
        bookingRepository.save(Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build());

        List<Booking> bookings = bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsBeforeOrderByEndDesc(
                owner.getId(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        assertThat(bookings).hasSize(1);
    }

    @Test
    public void shouldFindByItemOwnerIdAndEndIsBeforeOrderByEndDesc() {
        bookingRepository.save(Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build());

        List<Booking> bookings = bookingRepository.findByItemOwnerIdAndEndIsBeforeOrderByEndDesc(
                owner.getId(),
                LocalDateTime.now()
        );

        assertThat(bookings).hasSize(1);
    }
}
