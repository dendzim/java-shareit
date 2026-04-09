package ru.practicum.shareit.client;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.dto.ItemRequestDto;

@Component
public class ItemRequestClient extends BaseClient {

    public ItemRequestClient() {
        super("/requests");
    }

    public Object addItemRequest(ItemRequestDto itemRequest) {
        return post("", itemRequest);
    }

    public Object getAllRequests() {
        return get("/all");
    }

    public Object getMyRequests(Long ownerId) {
        return get("", ownerId);
    }

    public Object getReqestById(Long id) {
        return get("/" + id);
    }
}
