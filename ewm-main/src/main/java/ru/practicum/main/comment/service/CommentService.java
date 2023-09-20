package ru.practicum.main.comment.service;

import ru.practicum.main.comment.dto.RequestCommentDto;
import ru.practicum.main.comment.dto.ResponseCommentDto;

import java.util.List;

public interface CommentService {

    ResponseCommentDto create(RequestCommentDto newComment, Long userId, Long eventId);

    void deleteByIdByUser(Long userId, Long commentId);

    ResponseCommentDto update(RequestCommentDto newComment, Long userId, Long commentId);

    List<ResponseCommentDto> getAllCommentsForEvent(Long eventId, String keyword, Integer from, Integer size);

    ResponseCommentDto getByIdByUser(Long userId, Long commentId);

    List<ResponseCommentDto> getUsersComments(Long userId);

    void deleteByIdByAdmin(Long commentId);

    ResponseCommentDto getByIdByAdmin(Long commentId);

}
