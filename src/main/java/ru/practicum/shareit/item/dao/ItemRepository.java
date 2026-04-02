package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("""
            SELECT i
            FROM Item i
            WHERE i.isAvailable = true
            AND (LOWER(i.name) LIKE LOWER(%:text%)
                OR LOWER(i.description) LIKE LOWER(%:text%))
            """)
    Collection<Item> searchByNameOrDescription(String text);

    List<Item> findByOwnerId(Long userId);
}
