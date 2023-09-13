package ru.practicum.main.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.main.error.exception.CantDoException;
import ru.practicum.main.error.exception.EntityNotExistException;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class KingHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorEntity> incorrectRequestHandle(final ConstraintViolationException e) {
        ;
        String message = "Неправильно сделанный запрос";
        log.warn("Ошибка валидации. {}.", message);
        return new ResponseEntity<>(new ErrorEntity(HttpStatus.BAD_REQUEST.getReasonPhrase(), message),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorEntity> invalidArgumentHandle(final MethodArgumentNotValidException e) {
        String message = "Неправильно сделанный запрос";
        log.warn("Ошибка валидации. {}.", message);
        return new ResponseEntity<>(new ErrorEntity(HttpStatus.BAD_REQUEST.getReasonPhrase(), message),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorEntity> notFoundHandle(final EntityNotExistException e) {
        String message = "Требуемый объект не был найден";
        log.warn("{}. {}", message, e.getMessage());
        return new ResponseEntity<>(new ErrorEntity(HttpStatus.NOT_FOUND.getReasonPhrase(), message),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorEntity> dbErrorHandle(final DataIntegrityViolationException e) {
        String message = "Требуемый объект не был найден";
        log.warn("{}. {}", message, e.getMessage());
        return new ResponseEntity<>(new ErrorEntity(HttpStatus.CONFLICT.getReasonPhrase(), message),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorEntity> forbiddenHandle(final CantDoException e) {
        String message = "Для запрошенной операции условия не выполнены";
        log.warn("{}. {}", message, e.getMessage());
        return new ResponseEntity<>(new ErrorEntity(HttpStatus.FORBIDDEN.getReasonPhrase(), message),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorEntity> invalidBodyHandle(final HttpMessageNotReadableException e) {
        String message = "Недопустимый текст запроса";
        log.warn("{}. {}", message, e.getMessage());
        return new ResponseEntity<>(new ErrorEntity(HttpStatus.CONFLICT.getReasonPhrase(), message),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorEntity> badRequestHandle(final IllegalStateException e) {
        String message = "Неправильно сделанный запрос";
        log.warn("{}. {}", message, e.getMessage());
        return new ResponseEntity<>(new ErrorEntity(HttpStatus.BAD_REQUEST.getReasonPhrase(), message),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorEntity> missingParamHandle(final MissingServletRequestParameterException e) {
        String message = "Неправильно сделанный запрос";
        log.warn("{}. {}", message, e.getMessage());
        return new ResponseEntity<>(new ErrorEntity(HttpStatus.BAD_REQUEST.getReasonPhrase(), message),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorEntity> unexpectedErrorHandle(final Throwable e) {
        String message = "Произошла непредвиденная ошибка";
        log.warn("{}. {}", message, e.getMessage());
        return new ResponseEntity<>(
                new ErrorEntity(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), message),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
