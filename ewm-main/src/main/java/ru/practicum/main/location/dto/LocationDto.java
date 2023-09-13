package ru.practicum.main.location.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

    @NotNull
    private Float lat;

    @NotNull
    private Float lon;

}
