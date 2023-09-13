package ru.practicum.main.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EventConfirmedRequests {

    private Long eventId;
    private Long confirmed;

}
