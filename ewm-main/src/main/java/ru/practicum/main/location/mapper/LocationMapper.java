package ru.practicum.main.location.mapper;

import org.mapstruct.Mapper;
import ru.practicum.main.location.dto.LocationDto;
import ru.practicum.main.location.model.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    Location toLocation(LocationDto locationDto);

}
