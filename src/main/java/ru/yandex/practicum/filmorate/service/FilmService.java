package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.module.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmsStorage;

import java.util.List;

@Service

public class FilmService {


    private final FilmsStorage filmsStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmsStorage filmsStorage) {
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
            throw new FilmNotFoundException("Фильм с id " + film.getId() + " не найден");
        }
        return filmsStorage.updateFilm(film);
    }


    public void likeFilm(int id, int userId) {
        Film film = filmsStorage.getFilm(id);
        //film.likeFilm(userId);
        filmsStorage.addLike(id, userId);
    }

    public void removeLike(int id, int userId) {
        Film film = filmsStorage.getFilm(id);
        //film.removeLike(userId);
        filmsStorage.removeLike(id, userId);
    }

    public List<Film> getMostLikedFilms(int count) {
        /*if (filmsStorage.getFilms().isEmpty()) {
            return new ArrayList<>();
        }
        if (filmsStorage.getFilms().size() <= count) {
            return new ArrayList<>(filmsStorage.getFilms());
        }
        List<Film> values = filmsStorage.getFilms().stream()
                .sorted((e1, e2) ->
                        Integer.compare(e1.getLikes().size(), e2.getLikes().size()))
                .collect(Collectors.toList());

        Collections.reverse(values);
        return values.subList(0, count);*/
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
