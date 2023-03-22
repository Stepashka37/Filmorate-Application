package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.module.Film;

import java.util.List;

public interface FilmsStorage {

    Film getFilm(Long id);

    List<Film> getFilms();

    Film addFilm(Film film) throws ValidationException;

    Film updateFilm(Film film) throws ValidationException;

    void deleteAllFilms();

    void deleteFilm(Long id);
}
