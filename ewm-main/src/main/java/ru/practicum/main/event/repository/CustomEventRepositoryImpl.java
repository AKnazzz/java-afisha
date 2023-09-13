package ru.practicum.main.event.repository;

import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.EventSort;
import ru.practicum.main.event.model.EventState;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;


public class CustomEventRepositoryImpl implements CustomEventRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Event> findEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);
        Predicate predicate = cb.conjunction();

        if (users != null && !users.isEmpty()) {
            predicate = cb.and(predicate, root.get("initiator").in(users));
        }
        if (states != null && !states.isEmpty()) {
            predicate = cb.and(predicate, root.get("state").in(states));
        }
        if (categories != null && !categories.isEmpty()) {
            predicate = cb.and(predicate, root.get("category").in(categories));
        }
        if (rangeStart != null && rangeEnd != null) {
            predicate = cb.and(predicate, cb.between(root.get("eventDate"), rangeStart, rangeEnd));
        }

        return entityManager
                .createQuery(query.select(root).where(predicate))
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<Event> findEventsByPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd, Integer from, Integer size, EventSort sort) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);
        Predicate predicate = cb.conjunction();

        if (text != null && !text.isBlank()) {
            Predicate annotation = cb.like(cb.lower(root.get("annotation")), "%" + text.toLowerCase() + "%");
            Predicate description = cb.like(cb.lower(root.get("description")), "%" + text.toLowerCase() + "%");
            predicate = cb.and(predicate, cb.or(annotation, description));
        }

        if (categories != null && !categories.isEmpty()) {
            predicate = cb.and(predicate, root.get("category").in(categories));
        }
        if (paid != null) {
            predicate = cb.and(predicate, root.get("paid").in(paid));
        }

        if (rangeStart != null && rangeEnd != null) {
            predicate = cb.and(predicate, cb.between(root.get("eventDate"), rangeStart, rangeEnd));
        } else {
            predicate = cb.and(predicate, cb.greaterThan(root.get("eventDate"), LocalDateTime.now()));
        }

        predicate = cb.and(predicate, cb.equal(root.get("state"), EventState.PUBLISHED));

        query.select(root).where(predicate);
        if (sort != null && sort.equals(EventSort.EVENT_DATE)) {
            query.orderBy(cb.asc(root.get("eventDate")));
        }

        return entityManager
                .createQuery(query)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();
    }

}
