package ru.practicum.stats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.model.EndpointHit;

@Mapper(componentModel = "spring")
public interface EndpointHitMapper {

    @Mapping(target = "id", ignore = true)
    EndpointHit toEndpointHit(EndpointHitDto endpointHitDto);

}
