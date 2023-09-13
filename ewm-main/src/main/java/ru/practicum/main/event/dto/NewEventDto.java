package ru.practicum.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ru.practicum.main.event.validator.annotation.ValidEventDate;
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
@FieldDefaults(level = AccessLevel.PROTECTED)
public class NewEventDto {

    @NotBlank(message = "Annotation cannot be blank", groups = Create.class)
    @Size(min = 20, max = 2000, message = "The event annotation must be from 20 to 2000 characters",
            groups = {Create.class, Update.class, UpdateAdm.class})
    String annotation;

    @NotNull(message = "Category cannot be null", groups = Create.class)
    Long category;

    @NotBlank(message = "Description cannot be blank", groups = Create.class)
    @Size(min = 20, max = 7000, message = "The event description must be from 20 to 7000 characters",
            groups = {Create.class, Update.class, UpdateAdm.class})
    String description;

    @NotNull(message = "Event date cannot be null", groups = Create.class)
    @JsonFormat(pattern = DATE_PATTERN)
    @ValidEventDate(isAdmin = false, groups = {Create.class, Update.class})
    @ValidEventDate(isAdmin = true, groups = {UpdateAdm.class})
    LocalDateTime eventDate;

    @Valid
    LocationDto location;

    Boolean paid;

    @PositiveOrZero(message = "ParticipantLimit cannot be negative")
    Integer participantLimit;

    Boolean requestModeration;

    @NotBlank(message = "Title cannot be blank or null", groups = Create.class)
    @Size(min = 3, max = 120, message = "The event title must be from 3 to 120 characters",
            groups = {Create.class, Update.class, UpdateAdm.class})
    String title;

}

