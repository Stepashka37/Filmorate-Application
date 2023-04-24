package ru.yandex.practicum.filmorate.module;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class Rating {
    private int id;
    private String name;
}
