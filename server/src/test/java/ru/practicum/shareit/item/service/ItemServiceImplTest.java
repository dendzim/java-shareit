package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    private UserRepository userRepository;

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
}
