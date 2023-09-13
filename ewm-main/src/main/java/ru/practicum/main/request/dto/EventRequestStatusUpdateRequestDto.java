package ru.practicum.main.request.dto;

import lombok.*;
import ru.practicum.main.request.model.RequestProcessedState;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateRequestDto {

    @NotNull
    @NotEmpty
    private List<Long> requestIds;

    @NotNull
    private RequestProcessedState status;

}
