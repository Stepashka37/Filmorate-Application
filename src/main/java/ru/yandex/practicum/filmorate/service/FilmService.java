package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.module.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmsStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UsersStorage;

import java.util.List;

@Service

public class FilmService {

    private final UsersStorage usersStorage;
    private final FilmsStorage filmsStorage;

    @Autowired
    public FilmService(@Qualifier("userDbStorage") UsersStorage usersStorage, @Qualifier("filmDbStorage") FilmsStorage filmsStorage) {
        this.usersStorage = usersStorage;
        this.filmsStorage = filmsStorage;
    }

    public List<Film> getFilms() {
        return filmsStorage.getFilms();
    }

    public Film addFilm(Film film) {
        return filmsStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        if (filmsStorage.getFilm(film.getId()) == null) {
            throw new UserNotFoundException("Фильм с id" + film.getId() + " не найден");
        }
        return filmsStorage.updateFilm(film);
    }


    public void likeFilm(int id, int userId) {
        usersStorage.getUser(userId);
        filmsStorage.getFilm(id);
        filmsStorage.addLike(id, userId);
    }

    public void removeLike(int id, int userId) {
        usersStorage.getUser(userId);
        filmsStorage.getFilm(id);
        filmsStorage.removeLike(id, userId);
    }

    public List<Film> getMostLikedFilms(int count) {
        if (count > filmsStorage.getFilms().size()) {
            return filmsStorage.getFilms();
        }
        return filmsStorage.getPopularFilms(count);
    }

    public Film getFilm(int id) {
        return filmsStorage.getFilm(id);
    }

    public void deleteFilm(int id) {
        filmsStorage.deleteFilm(id);
    }

    public void deleteAllFilms() {
        filmsStorage.deleteAllFilms();
    }

    public List<Film> getPopularByGenreAndYear(int year, int genreId, int count) {
        if (year == 0 && genreId == 0) {
            return filmsStorage.getPopularFilms(count);
        } else if (genreId == 0) {
            return filmsStorage.getPopularByYear(year, count);
        } else if (year == 0) {
            return filmsStorage.getPopularByGenre(genreId, count);
        } else {
            return filmsStorage.getPopularByGenreAndYear(year, genreId, count);
        }
    }

    public List<Film> getCommonFilms(long id, long friendId) {
        return filmsStorage.getCommonFilms(id, friendId);
    }
}
