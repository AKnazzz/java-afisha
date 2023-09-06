package ru.practicum.stats.service;

import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    EndpointHitDto createHit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getAllStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
