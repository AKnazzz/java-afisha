package ru.practicum.main.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.comment.dto.RequestCommentDto;
import ru.practicum.main.comment.dto.ResponseCommentDto;
import ru.practicum.main.comment.mapper.CommentMapper;
import ru.practicum.main.comment.model.Comment;
import ru.practicum.main.comment.repository.CommentRepository;
import ru.practicum.main.error.exception.EntityNotExistException;
import ru.practicum.main.error.exception.CantDoException;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.EventState;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public ResponseCommentDto create(RequestCommentDto newComment, Long userId, Long eventId) {
        Event event = checkerEventExistsById(eventId);
        User user = checkerUserExistsById(userId);
        checkerPublishedExist(event);
        Comment comment = commentRepository.save(commentMapper.commentDtotoComment(newComment, user, event));
        ResponseCommentDto responseCommentDto = commentMapper.toResponseCommentDto(comment);
        log.info("Создан новый Comment {} от User c ID {} на Event c ID {}.", newComment, userId, eventId);
        return responseCommentDto;
    }

    @Override
    @Transactional
    public ResponseCommentDto update(RequestCommentDto newComment, Long userId, Long commentId) {
        checkerUserExistById(userId);
        Comment comment = checkerOwnCommentExistsAndGetById(userId, commentId);
        comment.setMessage(newComment.getMessage());
        ResponseCommentDto responseCommentDto = commentMapper.toResponseCommentDto(commentRepository.save(comment));
        log.info("Обновлён Comment c ID {} от User c ID {} на параметры {}.", commentId, userId, newComment);
        return responseCommentDto;
    }

    @Override
    @Transactional
    public void deleteByIdByAdmin(Long commentId) {
        checkerCommentExistsById(commentId);
        commentRepository.deleteById(commentId);
        log.info("Удалён Comment c ID {} администратором (User).", commentId);
    }

    @Override
    @Transactional
    public void deleteByIdByUser(Long userId, Long commentId) {
        checkerOwnCommentExistsAndGetById(userId, commentId);
        commentRepository.deleteById(commentId);
        log.info("Удалён Comment c ID {} от User c ID {}.", commentId, userId);
    }

    @Override
    public ResponseCommentDto getByIdByUser(Long userId, Long commentId) {
        ResponseCommentDto responseCommentDto = commentMapper.toResponseCommentDto(
                checkerOwnCommentExistsAndGetById(userId, commentId));
        log.info("Получен Comment c ID {} созданный User c ID {}.", commentId, userId);
        return responseCommentDto;
    }

    @Override
    public List<ResponseCommentDto> getAllCommentsForEvent(Long eventId, String keyword, Integer from, Integer size) {
        List<ResponseCommentDto> responseCommentDtos = commentMapper.toResponseCommentDto(commentRepository
                .findAllCommentsForEvent(eventId, keyword, from, size));
        log.info("Получен список всех Comments для Event c ID {} с параметрами keyword: {},  from: {}, size: {}.",
                eventId, keyword, from, size);
        return responseCommentDtos;
    }

    @Override
    public List<ResponseCommentDto> getUsersComments(Long userId) {
        List<ResponseCommentDto> responseCommentDtos = commentMapper.toResponseCommentDto(
                commentRepository.getCommentsByAuthorId(userId));
        log.info("Получен список всех Comments созданных User c ID {}.", userId);
        return responseCommentDtos;
    }

    @Override
    public ResponseCommentDto getByIdByAdmin(Long commentId) {
        ResponseCommentDto responseCommentDto = commentMapper.toResponseCommentDto(
                checkCommentExistsAndGetById(commentId));
        log.info("Получен Comment c ID {} администратором (User).", commentId);
        return responseCommentDto;
    }

    private Comment checkCommentExistsAndGetById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotExistException(Comment.class, commentId));
    }

    private void checkerCommentExistsById(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new EntityNotExistException(Comment.class, commentId);
        }
    }

    private void checkerPublishedExist(Event event) {
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new CantDoException("Не удается создать Comment к неопубликованному Event.");
        }
    }

    private Comment checkerOwnCommentExistsAndGetById(Long userId, Long commentId) {
        Comment comment = checkCommentExistsAndGetById(commentId);
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new CantDoException("Не удается работать с чужим Comment.");
        }
        return comment;
    }

    private Event checkerEventExistsById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotExistException(Event.class, eventId));
    }

    private void checkerUserExistById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotExistException(User.class, userId);
        }
    }

    private User checkerUserExistsById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotExistException(User.class, userId));
    }

}
