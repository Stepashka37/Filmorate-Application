package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.module.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.EventStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmsStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UsersStorage;

import java.util.List;

@Service

public class FilmService {

    private final UsersStorage usersStorage;
    private final FilmsStorage filmsStorage;
    private final EventStorage eventStorage;

    @Autowired
    public FilmService(@Qualifier("userDbStorage") UsersStorage usersStorage, @Qualifier("filmDbStorage") FilmsStorage filmsStorage, EventStorage eventStorage) {
        this.usersStorage = usersStorage;
        this.filmsStorage = filmsStorage;
        this.eventStorage = eventStorage;
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
        eventStorage.addLike(userId, id);
    }

    public void removeLike(int id, int userId) {
        usersStorage.getUser(userId);
        filmsStorage.getFilm(id);
        filmsStorage.removeLike(id, userId);
        eventStorage.removeLike(userId, id);
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

}
