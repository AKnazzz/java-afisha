package ru.practicum.main.event.validator.annotation;

import ru.practicum.main.event.validator.EventDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ValidEventDates.class)
@Constraint(validatedBy = EventDateValidator.class)
public @interface ValidEventDate {
    String message() default "Дата Event должна быть как минимум на 2 часа позже текущего времени и не может быть нулевой.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean isAdmin() default false;
}