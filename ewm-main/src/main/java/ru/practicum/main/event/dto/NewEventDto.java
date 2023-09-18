package ru.practicum.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.practicum.main.event.validator.annotation.EventDateAnnotationValid;
import ru.practicum.main.location.dto.LocationDto;
import ru.practicum.main.validation.group.Create;
import ru.practicum.main.validation.group.Update;
import ru.practicum.main.validation.group.UpdateAdm;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.main.util.Constants.DATE_PATTERN;

@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {

    @NotBlank(message = "Аннотация не может быть пустой", groups = Create.class)
    @Size(min = 20, max = 2000, message = "Аннотация к событию должна содержать от 20 до 2000 символов",
            groups = {Create.class, Update.class, UpdateAdm.class})
    protected String annotation;

    @NotNull(message = "Категория не может быть нулевой", groups = Create.class)
    protected Long category;

    @NotBlank(message = "Описание не может быть пустым", groups = Create.class)
    @Size(min = 20, max = 7000, message = "Описание события должно содержать от 20 до 7000 символов",
            groups = {Create.class, Update.class, UpdateAdm.class})
    protected String description;

    @NotNull(message = "Дата события не может быть нулевой", groups = Create.class)
    @JsonFormat(pattern = DATE_PATTERN)
    @EventDateAnnotationValid(isAdmin = false, groups = {Create.class, Update.class})
    @EventDateAnnotationValid(isAdmin = true, groups = {UpdateAdm.class})
    protected LocalDateTime eventDate;

    @Valid
    protected LocationDto location;

    protected Boolean paid;

    @PositiveOrZero(message = "Лимит участников не может быть отрицательным")
    protected Integer participantLimit;

    protected Boolean requestModeration;

    @NotBlank(message = "Заголовок не может быть пустым или null", groups = Create.class)
    @Size(min = 3, max = 120, message = "Название мероприятия должно содержать от 3 до 120 символов",
            groups = {Create.class, Update.class, UpdateAdm.class})
    protected String title;

}

