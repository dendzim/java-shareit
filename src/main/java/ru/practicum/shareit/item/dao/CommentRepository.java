package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Comment;

import java.util.Collection;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Collection<Comment> findByItemId(Long itemId);

    @Query("SELECT c FROM Comment c JOIN FETCH c.item WHERE c.item.ownerId = ?1")
    List<Comment> findByItemOwnerId(Long ownerId);
}
