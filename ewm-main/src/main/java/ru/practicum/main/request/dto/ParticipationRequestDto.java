package ru.practicum.main.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.main.request.model.RequestState;

import java.time.LocalDateTime;

import static ru.practicum.main.util.Constants.DATE_PATTERN;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {

    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime created;
    private Long event;
    private Long id;
    private Long requester;
    private RequestState status;

}
