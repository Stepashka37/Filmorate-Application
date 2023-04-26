package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.module.Film;
import ru.yandex.practicum.filmorate.storage.dao.EventDao;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmsStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UsersStorage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service

public class FilmService {

    private final UsersStorage usersStorage;
    private final FilmsStorage filmsStorage;
    private final EventDao eventDao;

    @Autowired
    public FilmService(@Qualifier("userDbStorage") UsersStorage usersStorage, @Qualifier("filmDbStorage") FilmsStorage filmsStorage, EventDao eventDao) {
        this.usersStorage = usersStorage;
        this.filmsStorage = filmsStorage;
        this.eventDao = eventDao;
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
        eventDao.addLike(userId, id);
    }

    public void removeLike(int id, int userId) {
        usersStorage.getUser(userId);
        filmsStorage.getFilm(id);
        filmsStorage.removeLike(id, userId);
        eventDao.removeLike(userId, id);
    }

    public List<Film> getMostLikedFilms(int count) {
        if (count > filmsStorage.getFilms().size()) {
            return filmsStorage.getFilms();
        }
        return filmsStorage.getPopularFilms(count);
    }

    public List<Film> recommendFilms(Integer userId) {
        usersStorage.getUser(userId);
        return filmsStorage.recommendFilms(userId);
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

    public List<Film> getCommonFilms(int userId, int friendId) throws SQLException {
        if (usersStorage.checkBothUsersExist(userId, friendId)) {
            return filmsStorage.getCommonFilms(userId, friendId);
        } else {
            throw new UserNotFoundException("Пользователь не найден");

    public List<Film> searchFilms(String query, List<String> by) {
        if (by.size() == 1) {
            if (by.contains("director")) {
                return filmsStorage.getFilmByDirectorQuery(query);
            } else if (by.contains("title")) {
                return filmsStorage.getFilmByFilmQuery(query);
            }
        }

        if (by.size() == 2 && by.containsAll(List.of("director", "title"))) {
            List<Film> films = filmsStorage.getFilmByDirectorQuery(query);
            films.addAll(filmsStorage.getFilmByFilmQuery(query));
            return films;
        }
        return new ArrayList<>();

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

    public List<Film> getDirectorsFilms(int directorId, String sortBy) {
        return filmsStorage.getDirectorsFilms(directorId, sortBy);
    }

}
