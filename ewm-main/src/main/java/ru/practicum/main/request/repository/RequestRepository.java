package ru.practicum.main.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.main.event.model.EventConfirmedRequests;
import ru.practicum.main.request.model.Request;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findRequestsByIdIn(List<Long> ids);

    List<Request> findRequestsByRequesterId(Long userId);

    List<Request> findRequestsByEventInitiatorIdAndEventId(Long userId, Long eventId);

    Optional<Request> findRequestByRequesterIdAndEventId(Long userId, Long eventId);

    Optional<Request> findRequestsByRequesterIdAndId(Long userId, Long requestId);

    @Query("SELECT count(r) FROM Request r " +
            "WHERE r.event.id = ?1 AND " +
            "r.status = 'CONFIRMED'")
    Long getConfirmedRequests(Long eventId);

    @Query("SELECT new ru.practicum.main.event.model.EventConfirmedRequests(r.event.id, count(r.id)) " +
            "FROM Request r " +
            "WHERE r.event.id IN ?1 " +
            "AND r.status = 'CONFIRMED' " +
            "GROUP BY r.event.id")
    List<EventConfirmedRequests> getConfirmedRequests(List<Long> ids);

}
