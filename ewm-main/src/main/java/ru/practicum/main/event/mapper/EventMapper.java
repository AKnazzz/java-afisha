package ru.practicum.main.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.main.category.mapper.CategoryMapper;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.dto.EventShortDto;
import ru.practicum.main.event.dto.NewEventDto;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.EventState;
import ru.practicum.main.location.mapper.LocationMapper;
import ru.practicum.main.location.model.Location;
import ru.practicum.main.user.mapper.UserMapper;
import ru.practicum.main.user.model.User;

@Mapper(componentModel = "spring", uses = {
        UserMapper.class,
        CategoryMapper.class,
        LocationMapper.class
})
public interface EventMapper {

    @Mapping(target = "createdOn", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "category")
    Event toEvent(NewEventDto newEventDto, User initiator, Category category, Location location, EventState state);

    EventFullDto toEventFullDto(Event event, Long confirmedRequests, Long views);

    EventShortDto toEventShortDto(Event event, Long confirmedRequests, Long views);

}
