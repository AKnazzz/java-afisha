package ru.practicum.main.compilation.dto;

import lombok.*;
import ru.practicum.main.validation.group.Create;
import ru.practicum.main.validation.group.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CompilationRequestDto {

    private List<Long> events = new ArrayList<>();
    private Boolean pinned;

    @NotBlank(message = "Заголовок не может быть пустым", groups = Create.class)
    @Size(min = 1, max = 50, message = "Заголовок должен содержать от 1 до 50 символов",
            groups = {Create.class, Update.class})
    private String title;

}
