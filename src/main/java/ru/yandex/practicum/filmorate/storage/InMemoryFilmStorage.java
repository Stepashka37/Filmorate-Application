package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.module.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmsStorage;

import java.util.*;


@Component
public class InMemoryFilmStorage implements FilmsStorage {
    private Map<Integer, Film> films = new HashMap<>();
    private int genId = 0;


    @Override
    public Film getFilm(int id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("Фильм с id " + id + " не найден");
        }
        return films.get(id);
    }


    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }


    @Override
    public Film addFilm(Film film) {
       // validateFilm(film);
        ++genId;
        film.setId(genId);
        film.setLikes(new HashSet<>());
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        // validateFilm(film);
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException("Фильм с id " + film.getId() + " не найден");
        }
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    /*private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза - не раньше 28 декабря 1895 года");
        }

    }*/

    @Override
    public void deleteAllFilms() {
        films.clear();
        genId = 0;
    }

    @Override
    public void deleteFilm(int id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("Фильм с id " + id + " не найден");
        }
        films.remove(id);
    }

    @Override
    public void addLike(int filmId, int userId) {

    }

    @Override
    public void removeLike(int filmId, int userId) {

    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return null;
    }


}

