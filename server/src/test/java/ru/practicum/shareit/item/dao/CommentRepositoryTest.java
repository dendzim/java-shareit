package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentRepositoryTest {

    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private User author;
    private Item item;

    @BeforeEach
    void setUp() {
        User testUser1 = new User(null, "name1", "email1@test.com");
        User user = userRepository.save(testUser1);

        Item testItem1 = new Item(null, "name1", "desc1", true, user.getId(),
                null);
        item = itemRepository.save(testItem1);

        User testUser2 = new User(null, "name2", "email2@test.com");
        author = userRepository.save(testUser2);
    }

    @Test
    public void shouldFindByItemId() {
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setText("ertyuio");
        comment.setCreated(LocalDateTime.now());

        commentRepository.save(comment);

        Collection<Comment> comments = commentRepository.findByItemId(item.getId());

        assertThat(comments).hasSize(1);

        Comment c = comments.iterator().next();

        assertThat(c.getId()).isEqualTo(comment.getId());
        assertThat(c.getAuthor()).isEqualTo(author);
        assertThat(c.getItem()).isEqualTo(item);
    }

    @Test
    public void shouldFindByItemOwnerId() {
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setText("wertyu");
        comment.setCreated(LocalDateTime.now());

        commentRepository.save(comment);

        Collection<Comment> comments = commentRepository.findByItemOwnerId(item.getOwnerId());

        assertThat(comments).hasSize(1);

        Comment c = comments.iterator().next();

        assertThat(c.getId()).isEqualTo(comment.getId());
        assertThat(c.getAuthor()).isEqualTo(author);
        assertThat(c.getItem()).isEqualTo(item);
    }
}
