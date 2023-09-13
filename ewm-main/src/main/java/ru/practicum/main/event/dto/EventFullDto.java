package ru.practicum.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ru.practicum.main.event.model.EventState;
import ru.practicum.main.location.dto.LocationDto;

import java.time.LocalDateTime;

import static ru.practicum.main.util.Constants.DATE_PATTERN;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto extends EventShortDto {

    String description;
    LocationDto location;
    Integer participantLimit;
    Boolean requestModeration;
    EventState state;

    @JsonFormat(pattern = DATE_PATTERN)
    LocalDateTime publishedOn;

    @JsonFormat(pattern = DATE_PATTERN)
    LocalDateTime createdOn;

}
