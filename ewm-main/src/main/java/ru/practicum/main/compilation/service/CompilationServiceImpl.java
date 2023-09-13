package ru.practicum.main.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.compilation.dto.CompilationRequestDto;
import ru.practicum.main.compilation.dto.CompilationResponseDto;
import ru.practicum.main.compilation.mapper.CompilationMapper;
import ru.practicum.main.compilation.model.Compilation;
import ru.practicum.main.compilation.repository.CompilationRepository;
import ru.practicum.main.error.exception.EntityNotExistException;
import ru.practicum.main.event.dto.EventShortDto;
import ru.practicum.main.event.mapper.EventMapper;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.event.service.StatService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;
    private final StatService statService;

    @Override
    @Transactional
    public CompilationResponseDto create(CompilationRequestDto compilationRequestDto) {
        if (compilationRequestDto.getPinned() == null) {
            compilationRequestDto.setPinned(false);
        }
        Set<Event> events = eventsListToSet(eventRepository.findEventsByIdIn(compilationRequestDto.getEvents()));

        allEventsExist(events.size(), compilationRequestDto.getEvents().size());
        Compilation compilation = compilationRepository.save(
                compilationMapper.toCompilation(compilationRequestDto, events));
        log.info("Создана новая Compilation {}", compilationRequestDto);
        return compilationMapper.toCompilationDto(compilation, findViewsAndRequestsForEvents(events));
    }

    @Override
    @Transactional
    public CompilationResponseDto update(CompilationRequestDto compilationRequestDto, Long compId) {
        Compilation compilation = compExistsAndGet(compId);

        if (compilationRequestDto.getPinned() != null) {
            compilation.setPinned(compilationRequestDto.getPinned());
        }
        if (compilationRequestDto.getEvents() != null) {
            Set<Event> events = eventsListToSet(eventRepository.findEventsByIdIn(compilationRequestDto.getEvents()));
            allEventsExist(events.size(), compilationRequestDto.getEvents().size());
            compilation.setEvents(events);
        }
        if (compilationRequestDto.getTitle() != null) {
            compilation.setTitle(compilationRequestDto.getTitle());
        }

        log.info("Обновлена compilation с ID {} на параметры {}", compId, compilationRequestDto);
        return compilationMapper.toCompilationDto(compilation, findViewsAndRequestsForEvents(compilation.getEvents()));
    }

    @Override
    @Transactional
    public void delete(Long compId) {
        compExists(compId);
        log.info("Compilation с ID {} удалена.", compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationResponseDto getById(Long compId) {
        Compilation compilation = compExistsAndGet(compId);
        Set<Event> events = compilation.getEvents();
        CompilationResponseDto compilationResponseDto = compilationMapper.toCompilationDto(compilation,
                findViewsAndRequestsForEvents(events));
        log.info("Получена Compilation с ID {}.", compId);
        return compilationResponseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationResponseDto> getAll(Boolean pinned, Integer from, Integer size) {
        List<Compilation> compilations;

        if (pinned != null) {
            compilations = compilationRepository.findAllByPinnedIs(pinned, PageRequest.of(from / size, size));
        } else {
            compilations = compilationRepository.findAll(PageRequest.of(from / size, size)).toList();
        }

        log.info("Получен список compilations с параметрами pinned={}, from={}, size={}", pinned, from, size);
        List<CompilationResponseDto> result = new ArrayList<>();
        for (Compilation compilation : compilations) {
            result.add(compilationMapper.toCompilationDto(compilation,
                    findViewsAndRequestsForEvents(compilation.getEvents())));
        }
        return result;
    }

    private List<EventShortDto> findViewsAndRequestsForEvents(Set<Event> events) {
        Map<Long, Long> views = statService.getViews(new ArrayList<>(events));
        Map<Long, Long> confirmedRequests = statService.getConfirmedRequests(new ArrayList<>(events));

        List<EventShortDto> result = new ArrayList<>();
        for (Event event : events) {
            result.add(eventMapper.toEventShortDto(event, confirmedRequests.getOrDefault(event.getId(), 0L),
                    views.getOrDefault(event.getId(), 0L)));
        }
        return result;
    }

    private Set<Event> eventsListToSet(List<Event> events) {
        return new HashSet<>(events);
    }

    private void compExists(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new EntityNotExistException(Compilation.class, compId);
        }
    }

    private void allEventsExist(Integer found, Integer provided) {
        if (!found.equals(provided)) {
            throw new EntityNotExistException("Найдены не все compilations.");
        }
    }

    private Compilation compExistsAndGet(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotExistException(Compilation.class, compId));
    }

}
