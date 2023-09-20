package ru.practicum.main.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.main.comment.dto.RequestCommentDto;
import ru.practicum.main.comment.dto.ResponseCommentDto;
import ru.practicum.main.comment.model.Comment;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "created", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "author", source = "user")
    @Mapping(target = "id", ignore = true)
    Comment commentDtotoComment(RequestCommentDto requestCommentDto, User user, Event event);

    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "eventId", source = "event.id")
    ResponseCommentDto toResponseCommentDto(Comment comment);

    List<ResponseCommentDto> toResponseCommentDto(List<Comment> comments);

}
