package ru.practicum.stats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EndpointHitDto {

    Long id;

    @Size(max = 255, message = "Длина App ограничена кол-вом 255 символов")
    @NotBlank(message = "App не может быть пустым")
    String app;

    @Size(max = 255, message = "Длина uri ограничена кол-вом 255 символов")
    @NotBlank(message = "Uri не может быть пустым")
    String uri;

    @Size(max = 31, message = "Длина ip ограничена кол-вом 31 символов")
    @NotBlank(message = "Ip не может быть пустым")
    String ip;

    @Past(message = "Timestamp не может быть в будущем")
    @NotNull(message = "Timestamp не может быть пустым")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp;

}