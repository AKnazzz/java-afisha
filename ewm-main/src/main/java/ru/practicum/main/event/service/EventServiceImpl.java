package ru.practicum.main.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.category.repository.CategoryRepository;
import ru.practicum.main.error.exception.EntityNotExistException;
import ru.practicum.main.error.exception.CantDoException;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.dto.EventShortDto;
import ru.practicum.main.event.dto.NewEventDto;
import ru.practicum.main.event.dto.UpdateEventDto;
import ru.practicum.main.event.mapper.EventMapper;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.EventSort;
import ru.practicum.main.event.model.EventState;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.location.mapper.LocationMapper;
import ru.practicum.main.location.model.Location;
import ru.practicum.main.location.repository.LocationRepository;
import ru.practicum.main.request.repository.RequestRepository;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final LocationMapper locationMapper;
    private final EventMapper eventMapper;
    private final StatService statService;

    @Override
    @Transactional
    public EventFullDto create(NewEventDto newEventDto, Long userId) {
        if (newEventDto.getPaid() == null) {
            newEventDto.setPaid(false);
        }
        if (newEventDto.getParticipantLimit() == null) {
            newEventDto.setParticipantLimit(0);
        }
        if (newEventDto.getRequestModeration() == null) {
            newEventDto.setRequestModeration(true);
        }
        User initiator = checkIfUserExistsAndGet(userId);
        Category category = checkIfCategoryExistsAndGet(newEventDto.getCategory());
        Location location = findLocation(locationMapper.toLocation(newEventDto.getLocation()));

        Event event = eventMapper.toEvent(newEventDto, initiator, category, location, EventState.PENDING);
        log.info("Создан Event c id {}. ", event.getId());

        return mapToFullDtoWithViewsAndRequests(eventRepository.save(event));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getAllByInitiatorId(Long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        log.info("Получен список Events from={} size={} от инициатора User с id {}", from, size, userId);
        return mapToShortDtoWithViewsAndRequests(eventRepository.findEventsByInitiatorId(userId, pageable));
    }

    @Override
    @Transactional
    public EventFullDto updateByInitiator(UpdateEventDto updatedEvent, Long eventId, Long userId) {
        checkIfUserExists(userId);
        Event event = checkIfOwnEventExistsAndGet(eventId, userId);
        checkIfUserCanUpdate(event);
        updateNotNullFields(updatedEvent, event);

        if (updatedEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updatedEvent.getParticipantLimit());
        }

        if (updatedEvent.getStateAction() != null) {
            switch (updatedEvent.getStateAction()) {
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
            }
        }

        log.info("Обновлен Event с id {} от User c id {}", eventId, userId);
        return mapToFullDtoWithViewsAndRequests(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventFullDto updateByAdmin(UpdateEventDto updatedEvent, Long eventId) {
        Event event = checkIfEventExistsAndGet(eventId);
        checkIfAdminCanUpdate(event);
        updateNotNullFields(updatedEvent, event);

        if (updatedEvent.getParticipantLimit() != null) {
            checkNewLimit(updatedEvent.getParticipantLimit(), requestRepository.getConfirmedRequests(eventId));
            event.setParticipantLimit(updatedEvent.getParticipantLimit());
        }

        if (updatedEvent.getStateAction() != null) {
            switch (updatedEvent.getStateAction()) {
                case PUBLISH_EVENT:
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                    event.setState(EventState.CANCELED);
                    break;
            }
        }

        log.info("Обновлен Event с id {} от admin", eventId);
        return mapToFullDtoWithViewsAndRequests(eventRepository.save(event));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getAllEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                                  LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        log.info("Получен список events от admin с параметрами: users={}, states={}, categories={}, start={}, end={}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return mapToFullDtoWithViewsAndRequests(
                eventRepository.findEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventByPublic(Long eventId, String uri, String ip) {
        Event event = checkIfPublishedEventExistsAndGet(eventId);
        log.info("Получен event с id {} от User public", eventId);
        statService.hit(uri, ip);
        return mapToFullDtoWithViewsAndRequests(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getAllEventsByPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                                    LocalDateTime rangeEnd, Boolean onlyAvailable, EventSort sort, Integer from,
                                                    Integer size, String uri, String ip) {

        checkIfStartBeforeEnd(rangeStart, rangeEnd);

        List<Event> events = eventRepository.findEventsByPublic(text, categories, paid, rangeStart, rangeEnd, from, size, sort);

        Map<Long, Integer> eventLimits = new HashMap<>();
        events.forEach(e -> eventLimits.put(e.getId(), e.getParticipantLimit()));

        List<EventShortDto> eventsWithViewsAndRequests = mapToShortDtoWithViewsAndRequests(events);

        if (onlyAvailable) {
            eventsWithViewsAndRequests = eventsWithViewsAndRequests.stream()
                    .filter(e -> eventLimits.get(e.getId()) == 0 || eventLimits.get(e.getId()) > e.getConfirmedRequests())
                    .collect(Collectors.toList());
        }

        if (sort != null && sort.equals(EventSort.VIEWS)) {
            eventsWithViewsAndRequests.sort(Comparator.comparing(EventShortDto::getViews));
        }

        log.info("Получен список events от user public с параметрами: text={}, categories={}, paid={}, start={}, end={}, onlyAvailable={}," +
                "sort={}, from={}, size={}", text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        statService.hit(uri, ip);

        return eventsWithViewsAndRequests;
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventByIdAndInitiatorId(Long eventId, Long userId) {
        return mapToFullDtoWithViewsAndRequests(checkIfOwnEventExistsAndGet(eventId, userId));
    }

    private Event checkIfOwnEventExistsAndGet(Long eventId, Long userId) {
        return eventRepository.findEventByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new EntityNotExistException(Event.class, eventId));
    }

    private List<EventShortDto> mapToShortDtoWithViewsAndRequests(List<Event> events) {
        Map<Long, Long> views = statService.getViews(events);
        Map<Long, Long> confirmedRequests = statService.getConfirmedRequests(events);

        return events.stream()
                .map(e -> eventMapper.toEventShortDto(
                        e,
                        confirmedRequests.getOrDefault(e.getId(), 0L),
                        views.getOrDefault(e.getId(), 0L)))
                .collect(Collectors.toList());
    }

    private List<EventFullDto> mapToFullDtoWithViewsAndRequests(List<Event> events) {
        Map<Long, Long> views = statService.getViews(events);
        Map<Long, Long> confirmedRequests = statService.getConfirmedRequests(events);

        return events.stream()
                .map(e -> eventMapper.toEventFullDto(
                        e,
                        confirmedRequests.getOrDefault(e.getId(), 0L),
                        views.getOrDefault(e.getId(), 0L)))
                .collect(Collectors.toList());
    }

    private void updateNotNullFields(UpdateEventDto updatedEvent, Event event) {
        if (updatedEvent.getEventDate() != null) {
            event.setEventDate(updatedEvent.getEventDate());
        }
        if (updatedEvent.getAnnotation() != null) {
            event.setAnnotation(updatedEvent.getAnnotation());
        }
        if (updatedEvent.getPaid() != null) {
            event.setPaid(updatedEvent.getPaid());
        }
        if (updatedEvent.getRequestModeration() != null) {
            event.setRequestModeration(updatedEvent.getRequestModeration());
        }
        if (updatedEvent.getDescription() != null) {
            event.setDescription(updatedEvent.getDescription());
        }
        if (updatedEvent.getCategory() != null) {
            event.setCategory(checkIfCategoryExistsAndGet(updatedEvent.getCategory()));
        }
        if (updatedEvent.getLocation() != null) {
            event.setLocation(findLocation(locationMapper.toLocation(updatedEvent.getLocation())));
        }
        if (updatedEvent.getTitle() != null) {
            event.setTitle(updatedEvent.getTitle());
        }
    }

    private void checkNewLimit(Integer newLimit, Long confirmedReq) {
        if (newLimit != 0 && newLimit < confirmedReq) {
            throw new CantDoException("Новый лимит не может быть меньше количества подтвержденных запросов");
        }
    }

    private EventFullDto mapToFullDtoWithViewsAndRequests(Event event) {
        return mapToFullDtoWithViewsAndRequests(Collections.singletonList(event)).get(0);
    }

    private void checkIfUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotExistException(User.class, userId);
        }
    }

    private User checkIfUserExistsAndGet(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotExistException(User.class, userId));
    }

    private void checkIfStartBeforeEnd(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null && end.isBefore(start)) {
            throw new IllegalStateException("Неправильный временной интервал. Начальный параметр должен быть раньше конечного параметра");
        }
    }

    private Category checkIfCategoryExistsAndGet(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new EntityNotExistException(Category.class, catId));
    }

    private void checkIfUserCanUpdate(Event event) {
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new CantDoException("Можно изменить только отложенные или отмененные события");
        }
    }

    private void checkIfAdminCanUpdate(Event event) {
        if (!event.getState().equals(EventState.PENDING)) {
            throw new CantDoException("Не удается опубликовать событие, потому что оно находится в неправильном состоянии: PUBLISHED");
        }
    }

    private Event checkIfEventExistsAndGet(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotExistException(Event.class, eventId));
    }

    private Event checkIfPublishedEventExistsAndGet(Long eventId) {
        return eventRepository.getEventIfPublished(eventId)
                .orElseThrow(() -> new EntityNotExistException(Event.class, eventId));
    }

    private Location findLocation(Location location) {
        return locationRepository.findByLatAndLon(location.getLat(), location.getLon())
                .orElseGet(() -> locationRepository.save(location));
    }

}
