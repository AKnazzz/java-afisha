package ru.practicum.main.compilation.dto;

import lombok.*;
import ru.practicum.main.event.dto.EventShortDto;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CompilationResponseDto {

    private Long id;
    private List<EventShortDto> events;
    private Boolean pinned;
    private String title;

}
