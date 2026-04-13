package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRepositoryTest {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Test
    public void shouldFindByOwnerId() {
        User testUser = new User(1L, "name", "email@test.com");
        User user = userRepository.save(testUser);

        Item testItem1 = new Item(1L, "name1", "desc1", true, 1L, null);
        Item item1 = itemRepository.save(testItem1);

        Item testItem2 = new Item(2L, "name2", "desc2", true, 1L, null);
        Item item2 = itemRepository.save(testItem2);

        Collection<Item> items = itemRepository.findByOwnerId(user.getId());

        assertThat(items).hasSize(2);
        assertThat(items).extracting(Item::getId).containsExactlyInAnyOrder(item1.getId(), item2.getId());
    }
}