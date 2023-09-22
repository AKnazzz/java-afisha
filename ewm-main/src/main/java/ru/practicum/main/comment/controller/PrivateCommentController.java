package ru.practicum.main.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.comment.dto.RequestCommentDto;
import ru.practicum.main.comment.dto.ResponseCommentDto;
import ru.practicum.main.comment.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@Slf4j
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class PrivateCommentController {

    private final CommentService commentService;

    @PostMapping("/{eventId}")
    public ResponseEntity<ResponseCommentDto> createComment(@RequestBody @Valid RequestCommentDto newComment,
            @PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Получен POST запрос по эндпоинту /users/{}/comments/{} на Event на создание Comment {}.", userId,
                eventId, newComment);
        return new ResponseEntity<>(commentService.create(newComment, userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<ResponseCommentDto> updateComment(@RequestBody @Valid RequestCommentDto newComment,
            @PathVariable Long userId, @PathVariable Long commentId) {
        log.info("Получен PATCH запрос по эндпоинту /users/{}/comments/{} на Event на апдейт Comment на параметры {}.",
                userId, commentId, newComment);
        return new ResponseEntity<>(commentService.update(newComment, userId, commentId), HttpStatus.OK);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<ResponseCommentDto> getCommentByIdForUser(@PathVariable Long userId,
            @PathVariable Long commentId) {
        log.info("Получен GET запрос по эндпоинту /users/{}/comments/{} на Event на получение Comment.", userId,
                commentId);
        return new ResponseEntity<>(commentService.getByIdByUser(userId, commentId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ResponseCommentDto>> getListCommentsForUsers(@PathVariable Long userId) {
        log.info(
                "Получен GET запрос по эндпоинту /users/{}/comments/ на Event на получение списка Comment конкретного User.",
                userId);
        return new ResponseEntity<>(commentService.getUsersComments(userId), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteCommentByIdForUser(@PathVariable Long userId, @PathVariable Long commentId) {
        commentService.deleteByIdByUser(userId, commentId);
        log.info("Получен DELETE запрос по эндпоинту /users/{}/comments/{} на удаление списка Comment с ID {}.",
                userId, commentId, commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
