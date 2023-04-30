package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.module.Film;
import ru.yandex.practicum.filmorate.module.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmsStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UsersStorage;

import java.sql.SQLException;
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
        }
    }

    public List<Film> getDirectorsFilms(int directorId, String sortBy) {
        return filmsStorage.getDirectorsFilms(directorId, sortBy);
    }

}
