package ru.practicum.main.error.exception;

public class CantDoException extends RuntimeException {

    public CantDoException(String message) {
        super(message);
    }

}
