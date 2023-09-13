package ru.practicum.main.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.main.util.Constants.DATE_PATTERN;

@Getter
@AllArgsConstructor
public class ErrorEntity {

    private String status;
    private String message;
    private final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_PATTERN));

}