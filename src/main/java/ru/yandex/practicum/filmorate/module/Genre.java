package ru.yandex.practicum.filmorate.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Genre {
    private Integer id;
    private String name;
}
