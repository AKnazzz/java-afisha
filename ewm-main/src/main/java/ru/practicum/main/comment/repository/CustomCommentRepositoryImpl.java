package ru.practicum.main.comment.repository;

import ru.practicum.main.comment.model.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class CustomCommentRepositoryImpl implements CustomCommentRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Comment> findAllCommentsForEvent(Long eventId, String keyword, Integer from, Integer size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Comment> query = cb.createQuery(Comment.class);
        Root<Comment> root = query.from(Comment.class);

        Predicate predicate = root.get("event").in(eventId);

        if (keyword != null && !keyword.isBlank()) {
            predicate = cb.and(predicate, cb.like(cb.lower(root.get("message")), "%" + keyword.toLowerCase() + "%"));
        }

        query.select(root).where(predicate);

        return entityManager
                .createQuery(query)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();
    }

}
