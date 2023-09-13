package ru.practicum.main.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    @Size(min = 2, max = 250, message = "Имя должно содержать от 20 до 2000 символов")
    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @Email(message = "Электронная почта должна соответствовать шаблону")
    @NotBlank(message = "Адрес электронной почты не может быть пустым")
    @Size(min = 6, max = 254, message = "Длина адреса электронной почты должна составлять от 6 до 254 символов")
    private String email;

}
