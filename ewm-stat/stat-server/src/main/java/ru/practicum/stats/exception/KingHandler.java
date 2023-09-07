package ru.practicum.stats.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class KingHandler {

    @ExceptionHandler
    public ResponseEntity<Error> illegalStateHandle(final IllegalStateException e) {
        log.warn("Перехвачено IllegalStateException.");
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Error> invalidArgumentHandle(final ConstraintViolationException e) {
        log.warn("Перехвачено ConstraintViolationException.");
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Error> missingRequestParameterHandle(final MissingServletRequestParameterException e) {
        log.warn("Перехвачено MissingServletRequestParameterException.");
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Error> unexpectedErrorHandle(final Throwable e) {
        log.warn("Перехвачено Throwable.");
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<Error> invalidArgumentHandle(final MethodArgumentNotValidException e) {
        log.warn("Перехвачено MethodArgumentNotValidException.");
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
