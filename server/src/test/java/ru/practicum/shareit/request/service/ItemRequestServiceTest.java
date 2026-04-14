package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    public void shouldGetRequest() {
        ItemRequest request = new ItemRequest(1L, "desc", 1L, LocalDate.now());
        Item item = mock(Item.class);

        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(itemRepository.findByRequestId(1L)).thenReturn(List.of(item));

        ItemRequestDto result = itemRequestService.getRequestById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        verify(requestRepository).findById(1L);
    }
}
