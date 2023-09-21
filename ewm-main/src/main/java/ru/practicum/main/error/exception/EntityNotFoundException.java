package ru.practicum.main.error.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class<?> entityClass, Long entityId) {
        super(entityClass.getSimpleName() + " c ID = " + entityId + " не найден.");
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

}
