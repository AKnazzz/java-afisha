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
    public List<Comment> findAllCommentsForEvent(Long eventId, String keyword, Integer fromIndex, Integer maxResults) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Comment> criteriaQuery = criteriaBuilder.createQuery(Comment.class);
        Root<Comment> commentRoot = criteriaQuery.from(Comment.class);
        Predicate predicate = criteriaBuilder.conjunction();

        predicate = criteriaBuilder.and(
                predicate,
                commentRoot.get("event").in(eventId)
        );

        if (keyword != null && !keyword.isBlank()) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.like(
                            criteriaBuilder.lower(commentRoot.get("message")),
                            "%" + keyword.toLowerCase() + "%"
                    )
            );
        }

        criteriaQuery.select(commentRoot).where(predicate);

        return entityManager
                .createQuery(criteriaQuery)
                .setFirstResult(fromIndex)
                .setMaxResults(maxResults)
                .getResultList();
    }

}
