package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.LastAndNextDate;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.CommentMapper;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.dao.CommentMapper.toCommentDto;
import static ru.practicum.shareit.item.dao.ItemMapper.toItem;
import static ru.practicum.shareit.item.dao.ItemMapper.toItemDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public ItemDto addItem(ItemDto itemDto, Long ownerId) {
        if (!userRepository.existsById(ownerId)) {
            log.warn("Владелец с указанным id " + ownerId + " не найден");
            throw new NotFoundException("Владелец с id " + ownerId + " не найден");
        }
        Item item = toItem(itemDto);
        item.setOwnerId(ownerId);
        return toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemDto itemDto,Long itemId,  long userId) {
        if (!userRepository.existsById(userId)) {
            log.warn("Пользователь с указанным id " + userId + " не найден");
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        Item newItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет с id " + itemId + " не найден"));
        newItem.setOwnerId(userId);

        if (itemDto.getName() != null) {
            newItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            newItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            newItem.setIsAvailable(itemDto.getAvailable());
        }
        return toItemDto(itemRepository.save(newItem));
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getItem(long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет с id " + itemId + " не найден"));

        ItemDto itemDto = toItemDto(item);
        Collection<Comment> comments = commentRepository.findByItemId(itemId);

        return addCommentsToItem(itemDto, comments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getAllOwnerItems(long ownerId) {
        List<Item> items = itemRepository.findByOwnerId(ownerId);

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> itemIds = items.stream()
                .map(Item::getId)
                .toList();

        List<Comment> comments = commentRepository.findByItemOwnerId(ownerId);
        Map<Long, List<CommentDto>> commentsMap = comments.stream()
                .collect(Collectors.groupingBy(
                        comment -> comment.getItem().getId(),
                        Collectors.mapping(CommentMapper::toCommentDto, Collectors.toList())
                ));

        List<LastAndNextDate> dates = bookingRepository.findLastAndNextDatesByOwnerId(ownerId);
        Map<Long, LastAndNextDate> datesMap = new HashMap<>();
        for (LastAndNextDate date : dates) {
            datesMap.put(date.getItemId(), date);
        }

        return items.stream()
                .map(item -> {
                    ItemDto dto = toItemDto(item);

                    LastAndNextDate bookingDates = datesMap.get(item.getId());
                    if (bookingDates != null) {
                        dto.setLastBooking(bookingDates.getLastBooking());
                        dto.setNextBooking(bookingDates.getNextBooking());
                    }

                    dto.setComments(commentsMap.getOrDefault(item.getId(), Collections.emptyList()));

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getNecessaryItem(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemRepository.searchByNameOrDescription(text)) {
            itemDtoList.add(toItemDto(item));
        }
        return itemDtoList;
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, Comment comment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет с id " + itemId + " не найден"));

        if (!bookingRepository.existsByItemIdAndBookerIdAndStatusIsAndEndBefore(
                itemId, userId, BookingStatus.APPROVED, LocalDateTime.now())) {
            throw new BadRequestException("Пользователь никогда не брал предмет в аренду");
        }
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        return toCommentDto(commentRepository.save(comment));
    }

    private ItemDto addCommentsToItem(ItemDto itemDto, Collection<Comment> comments ) {
        Collection<CommentDto> commentsDto = comments.stream()
                .map(CommentMapper::toCommentDto)
                .toList();
        itemDto.setComments(commentsDto);
        return itemDto;
    }
}
