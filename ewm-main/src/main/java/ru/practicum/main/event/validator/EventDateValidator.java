package ru.practicum.main.event.validator;

import ru.practicum.main.event.validator.annotation.ValidEventDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class EventDateValidator implements ConstraintValidator<ValidEventDate, LocalDateTime> {

    private boolean isAdmin;

    @Override
    public void initialize(ValidEventDate annotation) {
        isAdmin = annotation.isAdmin();
    }

    @Override
    public boolean isValid(LocalDateTime eventDate, ConstraintValidatorContext context) {
        if (eventDate == null) {
            return true;
        }

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime minValidEventDate = isAdmin ? currentTime.plusHours(1) : currentTime.plusHours(2);

        return eventDate.isAfter(minValidEventDate);
    }
}

