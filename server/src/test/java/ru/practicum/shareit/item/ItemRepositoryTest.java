package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRepositoryTest {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Test
    void shouldFindByOwnerId() {
        User testUser = new User(null, "name", "email@test.com");
        User savedUser = userRepository.save(testUser);

        Item testItem1 = new Item(null, "name1", "desc1", true, savedUser.getId(), null);
        Item item1 = itemRepository.save(testItem1);

        Item testItem2 = new Item(null, "name2", "desc2", true, savedUser.getId(), null);
        Item item2 = itemRepository.save(testItem2);

        Collection<Item> items = itemRepository.findByOwnerId(savedUser.getId());

        assertThat(items).hasSize(2);
        assertThat(items).extracting(Item::getId).containsExactlyInAnyOrder(item1.getId(), item2.getId());
    }

    @Test
    public void shouldFindAllByRequestIdIn() {
        User testUser1 = new User(null, "name1", "email1@test.com");
        User savedUser1 = userRepository.save(testUser1);

        ItemRequest request1 = new ItemRequest();
        request1.setRequestorId(savedUser1.getId());
        request1.setDescription("descript1");
        request1.setCreated(LocalDate.now());
        request1 = itemRequestRepository.save(request1);

        ItemRequest request2 = new ItemRequest();
        request2.setRequestorId(savedUser1.getId());
        request2.setDescription("descript2");
        request2.setCreated(LocalDate.now());
        request2 = itemRequestRepository.save(request2);

        User testUser2 = new User(null, "name", "email@test.com");
        User savedUser2 = userRepository.save(testUser2);

        Item testItem1 = new Item(null, "name1", "desc1", true, savedUser2.getId(),
                request1.getId());
        Item item1 = itemRepository.save(testItem1);

        Item testItem2 = new Item(null, "name2", "desc2", true, savedUser2.getId(),
                request2.getId());
        Item item2 = itemRepository.save(testItem2);

        Collection<Item> items = itemRepository.findAllByRequestIdIn(List.of(request1.getId(), request2.getId()));

        assertThat(items).hasSize(2);
        assertThat(items).extracting(Item::getId).containsExactlyInAnyOrder(item1.getId(), item2.getId());
    }

    @Test
    public void shouldSearchByDescription() {
        User testUser = new User(null, "name", "email@test.com");
        User savedUser = userRepository.save(testUser);

        Item testItem1 = new Item(null, "name1", "desc1", true, savedUser.getId(),
                null);
        Item item1 = itemRepository.save(testItem1);

        Collection<Item> items = itemRepository.searchByNameOrDescription(item1.getDescription());

        assertThat(items).hasSize(1);
        assertThat(items.iterator().next().getId()).isEqualTo(item1.getId());
    }
}