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

    void addScore(int filmId, int userId, int score);

    void removeScore(int filmId, int userId);

    List<Film> getPopularFilms(int count);

    List<Film> getCommonFilms(int userId, int friendId);

    List<Film> getFilmByDirectorQuery(String query);

    List<Film> getFilmByFilmQuery(String query);

    List<Film> getPopularByYear(int year, int count);

    List<Film> getPopularByGenreAndYear(int year, int genreId, int count);

    List<Film> getPopularByGenre(int genreId, int count);

    List<Film> getDirectorsFilms(int directorId, String sortBy);

    List<Film> recommendFilms(Integer userId);
}
