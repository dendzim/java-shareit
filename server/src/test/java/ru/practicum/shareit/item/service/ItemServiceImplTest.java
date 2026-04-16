package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.LastAndNextDate;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private ItemServiceImpl itemService;



    @Test
    public void shouldGetItem() {
        Item item = new Item(1L, "name", "desc", true, 1L, null);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(commentRepository.findByItemId(1L)).thenReturn(List.of());

        ItemDto result = itemService.getItem(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("name");
        verify(itemRepository).findById(1L);
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenGetItemWithInvalidId() {
        when(itemRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.getItem(999L))
                .isInstanceOf(NotFoundException.class);

        verify(itemRepository).findById(999L);
        verify(commentRepository, never()).findByItemId(anyLong());
    }

    @Test
    public void shouldSearchItems() {
        Item item = new Item(1L, "item", "description", true, 1L, null);
        when(itemRepository.searchByNameOrDescription("desc")).thenReturn(List.of(item));

        Collection<ItemDto> result = itemService.getNecessaryItem("desc");

        assertThat(result).hasSize(1);
        assertThat(result.iterator().next().getName()).isEqualTo("item");
        verify(itemRepository).searchByNameOrDescription("desc");
    }

    @Test
    public void shouldReturnEmptyListWhenSearchTextIsBlank() {
        Collection<ItemDto> result = itemService.getNecessaryItem("   ");

        assertThat(result).isEmpty();
        verify(itemRepository, never()).searchByNameOrDescription(any());
    }

    @Test
    public void shouldGetItems() {
        Item item = new Item(1L, "name", "desc", true, 1L, null);

        when(itemRepository.findByOwnerId(1L)).thenReturn(List.of(item));
        when(bookingRepository.findLastAndNextDatesByOwnerId(1L)).thenReturn(List.of());
        when(commentRepository.findByItemOwnerId(1L)).thenReturn(List.of());

        Collection<ItemDto> result = itemService.getAllOwnerItems(1L);

        assertThat(result).hasSize(1);
        verify(itemRepository).findByOwnerId(1L);
    }

    @Test
    public void shouldAddItem() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("item");
        itemDto.setDescription("desc");
        itemDto.setAvailable(true);
        itemDto.setRequestId(null);

        Item savedItem = new Item(1L, "item", "desc", true, 1L, null);

        when(userRepository.existsById(1L)).thenReturn(true);
        when(itemRepository.save(any(Item.class))).thenReturn(savedItem);

        ItemDto result = itemService.addItem(itemDto, 1L);

        assertThat(result.getId()).isEqualTo(1L);
        verify(userRepository).existsById(1L);
        verify(itemRepository).save(any());
    }

    @Test
    public void shouldAddItemWithRequestId() {
        Long userId = 1L;
        Long requestId = 10L;

        ItemDto itemDto = new ItemDto();
        itemDto.setName("item");
        itemDto.setDescription("desc");
        itemDto.setAvailable(true);
        itemDto.setRequestId(requestId);

        ItemRequest request = new ItemRequest();
        request.setId(requestId);

        Item savedItem = new Item(1L, "item", "desc", true, userId, requestId);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(itemRepository.save(any(Item.class))).thenReturn(savedItem);

        ItemDto result = itemService.addItem(itemDto, userId);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getRequestId()).isEqualTo(requestId);
        verify(itemRequestRepository).findById(requestId);
    }

    @Test
    public void shouldGetItemWithComments() {
        Long itemId = 1L;
        Long ownerId = 1L;
        Long authorId = 2L;

        Item item = Item.builder()
                .id(itemId)
                .name("name")
                .description("desc")
                .isAvailable(true)
                .ownerId(ownerId)
                .build();

        User author = User.builder()
                .id(authorId)
                .name("author")
                .email("author@test.com")
                .build();

        Comment comment1 = Comment.builder()
                .id(1L)
                .text("comment1")
                .item(item)
                .author(author)  // Добавляем автора
                .build();

        Comment comment2 = Comment.builder()
                .id(2L)
                .text("comment2")
                .item(item)
                .author(author)  // Добавляем автора
                .build();

        List<Comment> comments = List.of(comment1, comment2);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(commentRepository.findByItemId(itemId)).thenReturn(comments);

        ItemDto result = itemService.getItem(itemId);

        assertThat(result.getId()).isEqualTo(itemId);
        assertThat(result.getComments()).hasSize(2);
        assertThat(result.getComments()).extracting("text").containsExactly("comment1", "comment2");
        assertThat(result.getComments()).extracting("authorName").containsExactly("author", "author");
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenAddItemWithInvalidRequestId() {
        Long userId = 1L;
        Long requestId = 999L;

        ItemDto itemDto = new ItemDto();
        itemDto.setName("item");
        itemDto.setDescription("desc");
        itemDto.setAvailable(true);
        itemDto.setRequestId(requestId);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.addItem(itemDto, userId))
                .isInstanceOf(NotFoundException.class);

        verify(itemRepository, never()).save(any());
    }

    @Test
    public void shouldReturnEmptyListWhenOwnerHasNoItems() {
        Long ownerId = 1L;

        when(itemRepository.findByOwnerId(ownerId)).thenReturn(Collections.emptyList());

        Collection<ItemDto> result = itemService.getAllOwnerItems(ownerId);

        assertThat(result).isEmpty();
        verify(commentRepository, never()).findByItemOwnerId(anyLong());
        verify(bookingRepository, never()).findLastAndNextDatesByOwnerId(anyLong());
    }

    @Test
    public void shouldReturnEmptyListWhenSearchTextIsNull() {
        Collection<ItemDto> result = itemService.getNecessaryItem(null);

        assertThat(result).isEmpty();
        verify(itemRepository, never()).searchByNameOrDescription(any());
    }

    @Test
    public void shouldUpdateItem() {
        Long userId = 1L;
        Long itemId = 1L;

        Item currentItem = new Item(itemId, "old", "old", true, userId, null);
        ItemDto updateDto = new ItemDto();
        updateDto.setName("new");
        updateDto.setDescription("new");
        updateDto.setAvailable(false);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(currentItem));
        when(itemRepository.save(any(Item.class))).thenReturn(currentItem);

        ItemDto result = itemService.updateItem(updateDto, itemId, userId);

        assertThat(result.getName()).isEqualTo("new");
        assertThat(result.getDescription()).isEqualTo("new");
        assertThat(result.getAvailable()).isFalse();

        verify(userRepository).existsById(userId);
        verify(itemRepository).findById(itemId);
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenUpdateNonExistentItem() {
        Long userId = 1L;
        Long nonExistentItemId = 999L;
        ItemDto updateDto = new ItemDto();
        updateDto.setName("new");

        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemRepository.findById(nonExistentItemId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.updateItem(updateDto, nonExistentItemId, userId))
                .isInstanceOf(NotFoundException.class);

        verify(userRepository).existsById(userId);
        verify(itemRepository).findById(nonExistentItemId);
        verify(itemRepository, never()).save(any());
    }

    @Test
    public void shouldAddComment() {
        Long userId = 1L;
        Long itemId = 1L;
        String text = "comment";

        User user = new User(userId, "name", "email@test.com");
        Item item = new Item(itemId, "item", "description", true, 2L, null);

        Comment commentToSave = Comment.builder()
                .text(text)
                .build();

        Comment savedComment = Comment.builder()
                .id(1L)
                .text(text)
                .author(user)
                .item(item)
                .created(LocalDateTime.now())
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.existsByItemIdAndBookerIdAndStatusIsAndEndBefore(
                eq(itemId), eq(userId), eq(BookingStatus.APPROVED), any(LocalDateTime.class)))
                .thenReturn(true);
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        CommentDto result = itemService.addComment(itemId, userId, commentToSave);

        assertThat(result.getText()).isEqualTo(text);
        assertThat(result.getId()).isEqualTo(1L);

        verify(userRepository).findById(userId);
        verify(itemRepository).findById(itemId);
        verify(bookingRepository).existsByItemIdAndBookerIdAndStatusIsAndEndBefore(
                eq(itemId), eq(userId), eq(BookingStatus.APPROVED), any(LocalDateTime.class));
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void shouldThrowBadRequestExceptionWhenUserNeverBookedItem() {
        Long itemId = 1L;
        Long userId = 1L;
        Comment comment = new Comment();
        comment.setText("zwexrciytvuiubh");

        User user = new User(1L, "oleg", "oleg@test.com");
        Item item = new Item(itemId, "name", "desc", true, 1L, null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.existsByItemIdAndBookerIdAndStatusIsAndEndBefore(
                eq(itemId), eq(userId), eq(BookingStatus.APPROVED), any(LocalDateTime.class)))
                .thenReturn(false);

        BadRequestException exception = assertThrows(BadRequestException.class, () ->
                itemService.addComment(itemId, userId, comment)
        );

        assertThat(exception.getMessage()).isEqualTo("Пользователь никогда не брал предмет в аренду");

        verify(commentRepository, never()).save(any());
    }

    @Test
    public void shouldGetAllOwnerItemsWithBookingsAndComments() {
        Long ownerId = 1L;
        Long itemId1 = 1L;
        Long itemId2 = 2L;

        Item item1 = new Item(itemId1, "item1", "desc1", true, ownerId, null);
        Item item2 = new Item(itemId2, "item2", "desc2", true, ownerId, null);
        List<Item> items = List.of(item1, item2);

        User author = new User(2L, "author", "author@test.com");

        Comment comment1 = Comment.builder()
                .id(1L)
                .text("comment1")
                .item(item1)
                .author(author)
                .build();
        Comment comment2 = Comment.builder()
                .id(2L)
                .text("comment2")
                .item(item1)
                .author(author)
                .build();
        List<Comment> comments = List.of(comment1, comment2);

        LastAndNextDate date1 = mock(LastAndNextDate.class);
        when(date1.getItemId()).thenReturn(itemId1);
        when(date1.getLastBooking()).thenReturn(LocalDateTime.now().minusDays(1));
        when(date1.getNextBooking()).thenReturn(LocalDateTime.now().plusDays(1));

        LastAndNextDate date2 = mock(LastAndNextDate.class);
        when(date2.getItemId()).thenReturn(itemId2);
        when(date2.getLastBooking()).thenReturn(null);
        when(date2.getNextBooking()).thenReturn(LocalDateTime.now().plusDays(2));

        List<LastAndNextDate> dates = List.of(date1, date2);

        when(itemRepository.findByOwnerId(ownerId)).thenReturn(items);
        when(commentRepository.findByItemOwnerId(ownerId)).thenReturn(comments);
        when(bookingRepository.findLastAndNextDatesByOwnerId(ownerId)).thenReturn(dates);

        Collection<ItemDto> result = itemService.getAllOwnerItems(ownerId);

        assertThat(result).hasSize(2);

        ItemDto resultItem1 = result.stream().filter(i -> i.getId().equals(itemId1)).findFirst().get();
        assertThat(resultItem1.getComments()).hasSize(2);
        assertThat(resultItem1.getLastBooking()).isNotNull();
        assertThat(resultItem1.getNextBooking()).isNotNull();

        ItemDto resultItem2 = result.stream().filter(i -> i.getId().equals(itemId2)).findFirst().get();
        assertThat(resultItem2.getComments()).isEmpty();
        assertThat(resultItem2.getLastBooking()).isNull();
        assertThat(resultItem2.getNextBooking()).isNotNull();
    }
}
