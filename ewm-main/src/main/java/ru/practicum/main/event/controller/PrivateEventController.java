package ru.practicum.main.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.dto.EventShortDto;
import ru.practicum.main.event.dto.NewEventDto;
import ru.practicum.main.event.dto.UpdateEventDto;
import ru.practicum.main.event.service.EventService;
import ru.practicum.main.request.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.main.request.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.main.request.dto.ParticipationRequestDto;
import ru.practicum.main.request.service.RequestService;
import ru.practicum.main.validation.group.Create;
import ru.practicum.main.validation.group.Update;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {

    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<EventFullDto> create(@Validated(Create.class) @RequestBody NewEventDto newEventDto,
            @PathVariable Long userId) {
        log.info("Получен POST запрос по эндпоинту /users/{}/events на создание Event от User с ID {}.", userId,
                userId);
        return new ResponseEntity<>(eventService.create(newEventDto, userId), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getAllByInitiatorId(@PathVariable Long userId,
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        log.info("Получен GET запрос по эндпоинту /users/{}/events на получение списка Event от User с ID {}.", userId,
                userId);
        return new ResponseEntity<>(eventService.getAllByInitiatorId(userId, from, size), HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEventByIdAndInitiatorId(@PathVariable Long eventId,
            @PathVariable Long userId) {
        log.info("Получен GET запрос по эндпоинту /users/{}/events/{} на получение Event от User (инициатор) с ID {}.",
                userId, eventId, eventId);
        return new ResponseEntity<>(eventService.getEventByIdAndInitiatorId(eventId, userId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateByInitiator(
            @Validated(Update.class) @RequestBody UpdateEventDto updatedEvent,
            @PathVariable Long eventId,
            @PathVariable Long userId) {
        log.info(
                "Получен PATCH запрос по эндпоинту /users/{}/events/{} на обновление Event от User (инициатор) с ID {}.",
                userId, eventId, eventId);
        return new ResponseEntity<>(eventService.updateByInitiator(updatedEvent, eventId, userId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResultDto> processRequestsByInitiator(
            @RequestBody @Valid EventRequestStatusUpdateRequestDto updateRequest,
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        log.info(
                "Получен PATCH запрос по эндпоинту /users/{}/events/{}/requests на обновление Requests для Event от User (инициатор) с ID {}.",
                userId, eventId, eventId);
        return new ResponseEntity<>(requestService.processRequestsByInitiator(updateRequest, userId, eventId),
                HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getRequestsByInitiator(@PathVariable Long userId,
            @PathVariable Long eventId) {
        log.info(
                "Получен GET запрос по эндпоинту /users/{}/events/{}/requests на получение списка Requests для Event от User (инициатор) с ID {}.",
                userId, eventId, eventId);
        return new ResponseEntity<>(requestService.getRequestsByInitiator(userId, eventId), HttpStatus.OK);
    }

}
