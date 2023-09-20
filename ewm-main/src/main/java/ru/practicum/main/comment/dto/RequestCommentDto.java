package ru.practicum.main.comment.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RequestCommentDto {

    @NotBlank(message = "Сообщение не может быть пустым или нулевым.")
    @Size(min = 2, max = 1024, message = "Сообщение должно быть от 20 до 1024 символов.")
    private String message;

}
