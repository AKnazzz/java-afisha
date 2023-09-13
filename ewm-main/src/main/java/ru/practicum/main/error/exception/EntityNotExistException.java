package ru.practicum.main.error.exception;

public class EntityNotExistException extends RuntimeException {

    public EntityNotExistException(Class<?> entityClass, Long entityId) {
        super(entityClass.getSimpleName() + " c ID = " + entityId + " не найден.");
    }

    public EntityNotExistException(String message) {
        super(message);
    }

}
