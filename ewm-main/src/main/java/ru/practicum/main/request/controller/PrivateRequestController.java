package ru.practicum.main.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.request.dto.ParticipationRequestDto;
import ru.practicum.main.request.service.RequestService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class PrivateRequestController {

    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> create(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("Получен POST запрос по эндпоинту /users/{}/requests на создание нового Request на Event c ID {}.",
                userId, eventId);
        return new ResponseEntity<>(requestService.create(userId, eventId), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getRequestsByUserId(@PathVariable Long userId) {
        log.info("Получен GET запрос по эндпоинту /users/{}/requests на получение списка Request у User c ID {}.",
                userId, userId);
        return new ResponseEntity<>(requestService.getRequestsByUserId(userId), HttpStatus.OK);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancel(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info(
                "Получен PATCH запрос по эндпоинту /users/{}/requests на отмену Request у User c ID {} на Event c ID {}.",
                userId, userId, requestId);
        return new ResponseEntity<>(requestService.cancel(userId, requestId), HttpStatus.OK);
    }

}
