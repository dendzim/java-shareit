package ru.practicum.shareit.client;

import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.dto.ItemRequestDto;

import java.util.Collection;

public class ItemRequestClient extends BaseClient {
    public ItemRequestClient(RestTemplate rest) {
        super(rest);
    }

    public Object addItemRequest(ItemRequestDto itemRequest) {
        return null;
    }

    public Collection<ItemRequestDto> getAllRequests() {
        return null;
    }

    public Collection<ItemRequestDto> getMyReqests(Long ownerId) {
        return null;
    }

    public ItemRequestDto getReqestById(Long id) {
        return null;
    }
}
