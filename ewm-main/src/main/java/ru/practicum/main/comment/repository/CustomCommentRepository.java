package ru.practicum.main.comment.repository;

import ru.practicum.main.comment.model.Comment;

import java.util.List;

public interface CustomCommentRepository {

    List<Comment> findAllCommentsForEvent(Long eventId, String keyword, Integer from, Integer size);

}
