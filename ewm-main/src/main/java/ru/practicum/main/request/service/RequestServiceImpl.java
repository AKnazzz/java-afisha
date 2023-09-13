package ru.practicum.main.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.error.exception.EntityNotExistException;
import ru.practicum.main.error.exception.CantDoException;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.EventState;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.request.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.main.request.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.main.request.dto.ParticipationRequestDto;
import ru.practicum.main.request.mapper.RequestMapper;
import ru.practicum.main.request.model.Request;
import ru.practicum.main.request.model.RequestState;
import ru.practicum.main.request.repository.RequestRepository;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestMapper requestMapper;

    @Override
    @Transactional
    public ParticipationRequestDto create(Long userId, Long eventId) {
        User user = userExistsAndUserGet(userId);
        Event event = eventExistsAndGet(eventId);

        requestRepeatExist(userId, eventId);
        requestOwnerEventExist(userId, event);
        requestPublishedExist(event);
        participantLimitCheck(1, event);

        Request request = Request.builder()
                .created(LocalDateTime.now())
                .requester(user)
                .event(event)
                .status(RequestState.PENDING)
                .build();

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestState.CONFIRMED);
        }

        ParticipationRequestDto participationRequestDto = requestMapper
                .toParticipationRequestDto(requestRepository.save(request));

        log.info("Создан новый request с ID {}.", request.getId());
        return participationRequestDto;

    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequestsByUserId(Long userId) {
        userExist(userId);
        log.info("ПОлучен requests для user с ID {}", userId);
        return requestMapper.toParticipationRequestDto(requestRepository.findRequestsByRequesterId(userId));
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        userExist(userId);
        Request request = findRequestByUserIdAndRequestId(userId, requestId);
        request.setStatus(RequestState.CANCELED);
        log.info("User с ID {} отменил request с ID {}.", userId, requestId);
        return requestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResultDto processRequestsByInitiator(
            EventRequestStatusUpdateRequestDto updateRequest,
            Long userId, Long eventId) {
        log.info("Обновление request {} для user с ID {} для event с ID {}.", updateRequest, userId, eventId);

        userExist(userId);
        Event event = eventOwnerExistsAndGet(eventId, userId);
        List<Long> ids = updateRequest.getRequestIds();

        if (shouldSkipProcessing(ids, event)) {
            return new EventRequestStatusUpdateResultDto(
                    Collections.emptyList(),
                    Collections.emptyList()
            );
        }

        List<Request> requests = requestRepository.findRequestsByIdIn(ids);
        if (ids.size() != requests.size()) {
            throw new EntityNotExistException("Не все requests были найдены");
        }

        pendingCheck(requests);

        List<Request> confirmed = new ArrayList<>();
        List<Request> rejected = new ArrayList<>();

        switch (updateRequest.getStatus()) {
            case REJECTED:
                requests.forEach(r -> r.setStatus(RequestState.REJECTED));
                rejected = requestRepository.saveAll(requests);
                break;
            case CONFIRMED:
                participantLimitCheck(ids.size(), event);
                requests.forEach(r -> r.setStatus(RequestState.CONFIRMED));
                confirmed = requestRepository.saveAll(requests);
                break;
        }

        return new EventRequestStatusUpdateResultDto(
                requestMapper.toParticipationRequestDto(confirmed),
                requestMapper.toParticipationRequestDto(rejected)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequestsByInitiator(Long userId, Long eventId) {
        log.info("Получен список requests для user с ID {} для event с ID {}.", userId, eventId);
        return requestMapper.toParticipationRequestDto(
                requestRepository.findRequestsByEventInitiatorIdAndEventId(userId, eventId));
    }

    private Request findRequestByUserIdAndRequestId(Long userId, Long requestId) {
        return requestRepository.findRequestsByRequesterIdAndId(userId, requestId)
                .orElseThrow(() -> new EntityNotExistException(Request.class, requestId));
    }

    private void participantLimitCheck(Integer requestToAdd, Event event) {
        if (requestRepository.getConfirmedRequests(event.getId()) + requestToAdd > event.getParticipantLimit()
                && event.getParticipantLimit() != 0) {
            throw new CantDoException("Превышение лимита участников.");
        }
    }

    private void pendingCheck(List<Request> requests) {
        if (!requests.stream()
                .map(Request::getStatus)
                .allMatch(s -> s.equals(RequestState.PENDING))) {
            throw new CantDoException("Подтвержденные или отмененные requests не могут быть изменены.");
        }
    }

    private boolean shouldSkipProcessing(List<Long> numbersId, Event event) {
        return numbersId.isEmpty() || event.getParticipantLimit() == 0 || !event.getRequestModeration();
    }

    private Event eventOwnerExistsAndGet(Long eventId, Long userId) {
        return eventRepository.findByIdAndInitiatorIdAndLock(eventId, userId)
                .orElseThrow(() -> new EntityNotExistException(Event.class, eventId));
    }

    private Event eventExistsAndGet(Long eventId) {
        return eventRepository.findByIdAndLock(eventId)
                .orElseThrow(() -> new EntityNotExistException(Event.class, eventId));
    }

    private void requestOwnerEventExist(Long userId, Event event) {
        if (userId.equals(event.getInitiator().getId())) {
            throw new CantDoException("Не удается создать request на ваше собственное мероприятие.");
        }
    }

    private void requestPublishedExist(Event event) {
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new CantDoException("Не удается создать request на неопубликованное событие.");
        }
    }

    private void requestRepeatExist(Long userId, Long eventId) {
        if (requestRepository.findRequestByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new CantDoException("Невозможно дважды создавать одни и те же запросы на участие.");
        }
    }

    private User userExistsAndUserGet(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotExistException(User.class, userId));
    }

    private void userExist(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotExistException(User.class, userId);
        }
    }

}
