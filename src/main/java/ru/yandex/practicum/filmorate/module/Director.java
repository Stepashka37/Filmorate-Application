package ru.yandex.practicum.filmorate.module;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class Director {
    private int id;
    @NotBlank(message = "Имя не может быть пустым")
    @NotNull
    private String name;
}
