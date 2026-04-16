package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceTest {

    @Mock
    private ItemRequestRepository requestRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Test
    public void shouldAddItemRequest() {
        ItemRequest itemRequest = new ItemRequest();
        ItemRequest savedRequest = new ItemRequest(1L, "description", 1L, LocalDate.now());

        when(requestRepository.save(any())).thenReturn(savedRequest);

        ItemRequestDto result = itemRequestService.addItemRequest(itemRequest, 1L);

        assertThat(result.getId()).isEqualTo(1L);
        verify(requestRepository).save(any());
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenGetNonExistentRequest() {
        when(requestRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemRequestService.getRequestById(999L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void shouldGetRequest() {
        ItemRequest request = new ItemRequest(1L, "desc", 1L, LocalDate.now());
        Item item = mock(Item.class);

        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(itemRepository.findByRequestId(1L)).thenReturn(List.of(item));

        ItemRequestDto result = itemRequestService.getRequestById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        verify(requestRepository).findById(1L);
    }

    @Test
    public void shouldGetRequests() {
        ItemRequest request = new ItemRequest(1L, "ddrftgyhuesc", 1L, LocalDate.now());

        when(requestRepository.findByRequestorIdOrderByCreatedDesc(1L)).thenReturn(List.of(request));
        when(itemRepository.findAllByRequestIdIn(any())).thenReturn(List.of());

        Collection<ItemRequestDto> result = itemRequestService.getMyRequests(1L);

        assertThat(result).hasSize(1);
        verify(requestRepository).findByRequestorIdOrderByCreatedDesc(1L);
    }

    @Test
    public void shouldGetAllRequests() {
        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setDescription("dedfghsc");
        request.setRequestorId(1L);
        request.setCreated(LocalDate.now());

        when(requestRepository.findAllByOrderByCreatedDesc())
                .thenReturn(List.of(request));

        when(itemRepository.findAllByRequestIdIn(any()))
                .thenReturn(List.of());

        Collection<ItemRequestDto> result = itemRequestService.getAllRequests();

        assertThat(result).hasSize(1);

        verify(requestRepository).findAllByOrderByCreatedDesc();
        verify(itemRepository).findAllByRequestIdIn(any());
    }
}
