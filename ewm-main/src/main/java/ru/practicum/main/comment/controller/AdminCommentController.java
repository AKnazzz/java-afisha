package ru.practicum.main.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.comment.dto.ResponseCommentDto;
import ru.practicum.main.comment.service.CommentService;

@RestController
@Slf4j
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
public class AdminCommentController {

    private final CommentService commentService;

    @GetMapping("/{commentId}")
    public ResponseEntity<ResponseCommentDto> getCommentByIdForAdmin(@PathVariable Long commentId) {
        log.info("Получен GET запрос по эндпоинту /admin/comments/{} на получение Comment.", commentId);
        return new ResponseEntity<>(commentService.getByIdByAdmin(commentId), HttpStatus.OK);
    }


    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteCommentByIdForAdmin(@PathVariable Long commentId) {
        commentService.deleteByIdByAdmin(commentId);
        log.info("Получен DELETE запрос по эндпоинту /admin/comments/{} на удаление Comment с ID {}.", commentId,
                commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
