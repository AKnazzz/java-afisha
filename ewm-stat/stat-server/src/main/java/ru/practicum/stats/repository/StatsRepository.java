package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT new ru.practicum.stats.dto.ViewStatsDto(eh.app, eh.uri, COUNT(eh.ip)) " +
            "FROM EndpointHit eh " +
            "WHERE eh.timestamp between ?1 AND ?2 " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(eh.ip) DESC")
    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.stats.dto.ViewStatsDto(eh.app, eh.uri, COUNT(DISTINCT eh.ip)) " +
            "FROM EndpointHit eh " +
            "WHERE eh.timestamp between ?1 AND ?2 " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(DISTINCT eh.ip) DESC")
    List<ViewStatsDto> getStatsUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.stats.dto.ViewStatsDto(eh.app, eh.uri,COUNT(eh.ip)) " +
            "FROM EndpointHit eh " +
            "WHERE eh.timestamp between ?1 AND ?2 AND e.uri IN(?3)" +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(eh.ip) DESC")
    List<ViewStatsDto> getStatsForUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.stats.dto.ViewStatsDto(eh.app, eh.uri, COUNT(DISTINCT eh.ip)) " +
            "FROM EndpointHit eh " +
            "WHERE eh.timestamp between ?1 AND ?2 AND e.uri IN(?3)" +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(DISTINCT eh.ip) DESC")
    List<ViewStatsDto> getStatsUniqueIpForUris(LocalDateTime start, LocalDateTime end, List<String> uris);

}
