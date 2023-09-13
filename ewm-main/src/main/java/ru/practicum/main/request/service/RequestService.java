package ru.practicum.main.request.service;

import ru.practicum.main.request.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.main.request.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.main.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    ParticipationRequestDto create(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequestsByUserId(Long userId);

    ParticipationRequestDto cancel(Long userId, Long requestId);

    EventRequestStatusUpdateResultDto processRequestsByInitiator(EventRequestStatusUpdateRequestDto updateRequest,
                                                                 Long userId,
                                                                 Long eventId);

    List<ParticipationRequestDto> getRequestsByInitiator(Long userId, Long eventId);

}
