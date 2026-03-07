package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

@Repository
public interface ItemRepository extends JpaRepository<User, Long> {

    @Query("""
            SELECT i
            FROM Item i
            WHERE i.isAvailable = true
            AND (LOWER(i.name) LIKE LOWER(%:text%)
                OR LOWER(i.description) LIKE LOWER(%:text%))
            """)
    Collection<Item> searchByNameOrDescription(String text);

    Collection<Item> findByOwnerId(Long userId);
}
