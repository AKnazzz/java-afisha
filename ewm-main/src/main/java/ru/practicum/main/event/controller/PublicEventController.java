package ru.practicum.main.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.dto.EventShortDto;
import ru.practicum.main.event.model.EventSort;
import ru.practicum.main.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.main.util.Constants.DATE_PATTERN;

@Validated
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventController {

    private final EventService eventService;

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> getEventByPublic(@PathVariable Long id, HttpServletRequest request) {
        log.info("Получен GET запрос по эндпоинту /events на получение Event с ID {}.", id);
        return new ResponseEntity<>(eventService.getEventByPublic(id, request.getRequestURI(), request.getRemoteAddr()),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getAllEventsByPublic(@RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) EventSort sort,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size,
            HttpServletRequest request) {
        log.info("Получен GET запрос по эндпоинту /events на получение списка Events.");
        return new ResponseEntity<>(
                eventService.getAllEventsByPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                        from, size, request.getRequestURI(), request.getRemoteAddr()), HttpStatus.OK);
    }

}
