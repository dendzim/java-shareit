package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestRepositoryTest {

    private final ItemRequestRepository itemRequestRepository;

    private final UserRepository userRepository;

    @Test
    public void shouldFindByRequestorIdOrderByCreatedDesc() {
        User testUser = new User(1L, "name", "email@test.com");
        User user = userRepository.save(testUser);
        ItemRequest request = new ItemRequest();
        request.setRequestorId(user.getId());
        request.setDescription("test");
        request.setCreated(LocalDate.now());

        ItemRequest request1 = itemRequestRepository.save(request);

        Collection<ItemRequest> requests = itemRequestRepository.findByRequestorIdOrderByCreatedDesc(user.getId());

        assertThat(requests).hasSize(1);
        assertThat(requests.iterator().next().getId()).isEqualTo(request1.getId());
    }
}
