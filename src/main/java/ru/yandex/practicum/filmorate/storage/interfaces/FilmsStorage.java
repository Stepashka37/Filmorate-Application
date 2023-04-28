package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.module.Film;

import java.util.List;

public interface FilmsStorage {

    Film getFilm(int id);

    List<Film> getFilms();

    Film addFilm(Film film) throws ValidationException;

    Film updateFilm(Film film) throws ValidationException;

    void deleteAllFilms();

    void deleteFilm(int id);

    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);

    List<Film> getPopularFilms(int count);

    List<Film> getFilmByDirectorQuery(String query);

    List<Film> getFilmByFilmQuery(String query);

    List<Film> getDirectorsFilms(int directorId, String sortBy);

    List<Film> recommendFilms(Integer userId);

}
