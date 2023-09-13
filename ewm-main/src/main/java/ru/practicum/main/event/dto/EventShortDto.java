package ru.practicum.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.user.dto.UserShortDto;

import java.time.LocalDateTime;

import static ru.practicum.main.util.Constants.DATE_PATTERN;

@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {

    protected String annotation;
    protected CategoryDto category;
    protected Long confirmedRequests;

    @JsonFormat(pattern = DATE_PATTERN)
    protected LocalDateTime eventDate;

    protected Long id;
    protected UserShortDto initiator;
    protected Boolean paid;
    protected String title;
    protected Long views;

}
