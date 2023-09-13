package ru.practicum.main.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {

    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@Validated(Create.class) @RequestBody NewEventDto newEventDto, @PathVariable Long userId) {
        return eventService.create(newEventDto, userId);
    }

    @GetMapping
    public List<EventShortDto> getAllByInitiatorId(@PathVariable Long userId,
                                                   @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
                                                   @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        return eventService.getAllByInitiatorId(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventByIdAndInitiatorId(@PathVariable Long eventId, @PathVariable Long userId) {
        return eventService.getEventByIdAndInitiatorId(eventId, userId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateByInitiator(@Validated(Update.class) @RequestBody UpdateEventDto updatedEvent,
                                          @PathVariable Long eventId,
                                          @PathVariable Long userId) {
        return eventService.updateByInitiator(updatedEvent, eventId, userId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResultDto processRequestsByInitiator(@RequestBody @Valid EventRequestStatusUpdateRequestDto updateRequest,
                                                                        @PathVariable Long userId,
                                                                        @PathVariable Long eventId) {
        return requestService.processRequestsByInitiator(updateRequest, userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsByInitiator(@PathVariable Long userId, @PathVariable Long eventId) {
        return requestService.getRequestsByInitiator(userId, eventId);
    }

}
