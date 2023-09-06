package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.mapper.EndpointHitMapper;
import ru.practicum.stats.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

    private final StatsRepository repository;
    private final EndpointHitMapper mapper;

    @Override
    @Transactional
    public EndpointHitDto createHit(EndpointHitDto endpointHitDto) {
        repository.save(mapper.toEndpointHit(endpointHitDto));
        log.info("EndpointHit {} создан.", endpointHitDto);
        return endpointHitDto;
    }

    @Override
    public List<ViewStatsDto> getAllStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (start.isAfter(end)) {
            log.info("Некорректное время старта");
            throw new IllegalStateException("Некорректное время старта");
        }
        log.info("Получена stats с параметрами: start={}, end={}, uris={}, unique={}", start, end, uris, unique);
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                return repository.getStatsUniqueIp(start, end);
            } else {
                return repository.getStats(start, end);
            }
        } else {
            if (unique) {
                return repository.getStatsUniqueIpForUris(start, end, uris);
            } else {
                return repository.getStatsForUris(start, end, uris);
            }
        }
    }

}
