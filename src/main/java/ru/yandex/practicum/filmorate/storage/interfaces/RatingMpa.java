package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.module.Rating;

import java.util.List;

public interface RatingMpa {
    List<Rating> findALl();

    Rating findById(Integer id);
}
