package ru.practicum.main.event.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.dto.UpdateEventDto;
import ru.practicum.main.event.model.EventState;
import ru.practicum.main.event.service.EventService;
import ru.practicum.main.validation.group.UpdateAdm;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.main.util.Constants.DATE_PATTERN;

@Validated
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {

    private final EventService eventService;

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateByAdmin(
            @RequestBody @Validated(UpdateAdm.class) UpdateEventDto updatedEvent,
            @PathVariable Long eventId) {
        log.info("Получен PATCH запрос по эндпоинту /admin/events на обновление Event с ID {} на параметры {}.",
                eventId, updatedEvent);
        return new ResponseEntity<>(eventService.updateByAdmin(updatedEvent, eventId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getAllEventsByAdmin(@RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<EventState> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.info("Получен GET запрос по эндпоинту /admin/events на получение списка Event.");
        return new ResponseEntity<>(
                eventService.getAllEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size),
                HttpStatus.OK);
    }

}
