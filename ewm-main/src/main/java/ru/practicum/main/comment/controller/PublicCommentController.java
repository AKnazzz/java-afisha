package ru.practicum.main.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.comment.dto.ResponseCommentDto;
import ru.practicum.main.comment.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@Slf4j
@RequestMapping("/comments/{eventId}")
@RequiredArgsConstructor
public class PublicCommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<ResponseCommentDto>> getAllCommentsForEvent(@PathVariable Long eventId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.info("Получен GET запрос по эндпоинту /comments/{} на получение списка Comment на конкретный Event.",
                eventId);
        return new ResponseEntity<>(commentService.getAllCommentsForEvent(eventId, keyword, from, size), HttpStatus.OK);
    }

}
