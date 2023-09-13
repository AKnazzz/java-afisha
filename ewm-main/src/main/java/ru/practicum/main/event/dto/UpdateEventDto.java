package ru.practicum.main.event.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.practicum.main.event.model.EventStateAction;

@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventDto extends NewEventDto {

    private EventStateAction stateAction;

}
